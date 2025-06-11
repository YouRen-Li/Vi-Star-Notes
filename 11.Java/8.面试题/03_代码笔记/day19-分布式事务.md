# 1、本地事务回顾

## 1.1 什么是事务？

```
mysql事务：
	一组sql操作(不可分割) 要么都成功 要么都失败
redis事务：redis秒杀超卖问题(java多线程每个线程的一组redis命令会被其他线程插队执行修改数据)
	多个redis命令，按照先后顺序执行不会被插队
```

## 1.2 事务隔离级别

并发的事务导致的问题：

```
脏读： 一个事务可以读取另一个事务未提交的数据
不可重复读：一个事务执行期间，另一个事务修改了前一个事务的行数据提交了，前一个事务中最后再读取数据时发现跟之前不一样
幻读：一个事务执行期间，另一个事务新增或者删除了该表的行数据，前一个事务最后读取表数据时发现多了或者少了一些行
```

事务隔离级别

```
More Actions 事务隔离级别   脏读   不可重复读  幻读
读未提交（read-uncommitted）√√√
读已提交（read-committed）×√√
可重复读（repeatable-read）××√
串行化（serializable）×××
```

查看事务隔离级别：

```mysql
# 查询mysql的全局变量
SHOW VARIABLES;
# 过滤查询 事务相关的全局变量
SHOW VARIABLES LIKE '%trans%';
SELECT @@transaction_isolation;

SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

## 1.3 事务传播行为

spring为了解决复杂业务场景的事务处理(多个事务混合执行)，提供的事务传播行为

```java
//spring提供的事务管理注解
@Transactional(propagation=Propagation.REQUIRED)
//propagation:事务传播行为
//1、REQUIRED: 如果业务方法被调用时已经存在事务 直接使用，不存在开启新事务
//2、SUPPORTS：如果业务方法被调用时已经存在事务 直接使用，不存在不使用事务
//3、MANDATORY：如果业务方法被调用时已经存在事务 直接使用，不存在抛出异常
//4、REQUIRES_NEW：如果业务方法被调用时已经存在事务则挂起，自己新建事务执行
//5、NOT_SUPPORTED：如果业务方法被调用时已经存在事务则挂起，自己不使用事务执行
//6、NEVER：如果业务方法被调用时已经存在事务则抛出异常，不存在不使用事务执行
//7、NESTED：事务中基于savepoint方式 嵌套事务执行
```

使用最多的就是：REQUIRED、REQUIRES_NEW

```
a(required){
	b(required);
	c(requires_new);
	d(required);
	e(requires_new);
	// a方法的业务
}

//a业务出现异常：a、b、d回滚  c、e提交
//e方法出现异常：e、a、b、d回滚  c提交
```



# 2、事务案例搭建

## 2.1 数据库

导入课件中的 all_in_one.sql和seata.sql

## 2.2 项目搭建

测试springboot项目：2.3.6

mybatisx逆向工程

事务测试

业务

```
 	创建账户, 订单, 库存, 业务 四个服务。访问业务服务 下单接口，远程访问订单服务保存订单,远程访问库存服务减库存,远程访问账户服务修改用户账户余额同时记录日志。
 	
请求下单入口： 业务服务处理
	业务服务远程访问订单服务保存订单
	业务服务保存订单后远程访问库存服务
 	库存服务减库存后远程访问账户服务修改用户账户余额同时记录日志
 1、用户服务：
 	修改用户账户余额 记录操作日志
 2、库存服务：
 	扣减库存
 	远程访问账户服务 更新账户余额
 3、订单服务：
 	创建订单
 4、业务服务：
    接收前端的下单请求：
    	远程访问订单服务保存订单
        远程访问库存服务更新库存
        
```



# 3、分布式事务

## 3.1 why？

```
上面的案例，请求下单时 访问business的下单接口
	    	1、远程访问订单服务	保存订单(本地事务)
        	2、远程访问库存服务	更新库存(本地事务)
        		库存服务中又远程访问了账户服务扣账户余额(本地事务 有异常)
 执行1时 没有出现异常  订单服务保存订单的本地事务会提交
 执行2时 
 		2.1 更新库存没有异常 事务仍在执行
 		2.2 更新账户余额时  出现异常(账户服务的本地事务回滚 并抛出异常)
 		2.3 更新库存因为远程访问了 更新账户余额代码 接收到了异常 ，因为异常未处理
 			更新库存的事务回滚
 在分布式架构的项目中，以上的场景较多，我们希望他们作为一个整体 一起成功或失败
 
```

理论

解决方案



seata流程：2pc

```
两阶段提交协议的演变：

一阶段：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源。

二阶段：

提交异步化，非常快速地完成。
回滚通过一阶段的回滚日志进行反向补偿。
```



# 4、seata

## 4.1 搭建tc服务器

nacos中按照课件创建seata配置

数据库中需要导入seata.sql

双击解压后的seata/bin/startup.cmd



## 4.2 项目整合seata

在父工程中引入依赖

```
        <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
            <version>1.4.2</version>
        </dependency>
```

在所有项目的application.yml中添加

```
seata:
# 值必须和Seata在nacos中配置 的值一致
  tx-service-group: my_test_tx_group
```

修改代码：在business的 下单业务方法上添加全局事务注解

```
只需要在入口业务方法上添加@GlobalTransactional 注解
```

添加配置类：

```
Transactional注解是springtx提供的注解

如果希望它支持seata分布式事务，必须对数据库连接对象使用seata代理
```

```java
@Configuration
public class DataSourceConfig {
    //循环依赖：
    //@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
    @Autowired
    DruidDataSource druidDataSource;
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
     * @param druidDataSource The DruidDataSource
     * @return The default datasource
     */
    @Primary
    @Bean("dataSource")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }
}
```



分布式事务测试

seata原理

