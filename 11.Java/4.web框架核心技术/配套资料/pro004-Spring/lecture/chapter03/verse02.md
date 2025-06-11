[[toc]]

# 第二节 声明式事务概念



## 1、编程式事务

事务功能的相关操作全部通过自己编写代码来实现：

```java
Connection conn = ...;
	
try {
	
	// 开启事务：关闭事务的自动提交
	conn.setAutoCommit(false);
	
	// 核心操作
	
	// 提交事务
	conn.commit();
	
}catch(Exception e){
	
	// 回滚事务
	conn.rollBack();
	
}finally{
	
	// 释放数据库连接
	conn.close();
	
}
```



编程式的实现方式存在缺陷：

- 细节没有被屏蔽：具体操作过程中，所有细节都需要程序员自己来完成，比较繁琐。
- 代码复用性不高：如果没有有效抽取出来，每次实现功能都需要自己编写代码，代码就没有得到复用。



## 2、声明式事务

既然事务控制的代码有规律可循，代码的结构基本是确定的，所以框架就可以将固定模式的代码抽取出来，进行相关的封装。

封装起来后，我们只需要在配置文件中进行简单的配置即可完成操作。

- 好处1：提高开发效率
- 好处2：消除了冗余的代码
- 好处3：框架会综合考虑相关领域中在实际开发环境下有可能遇到的各种问题，进行了健壮性、性能等各个方面的优化

所以，我们可以总结下面两个概念：

- <span style="color:blue;font-weight:bold;">编程式</span>：<span style="color:blue;font-weight:bold;">自己写代码</span>实现功能
- <span style="color:blue;font-weight:bold;">声明式</span>：通过<span style="color:blue;font-weight:bold;">配置</span>让<span style="color:blue;font-weight:bold;">框架</span>实现功能



## 3、事务管理器

### ①顶级接口

#### [1]Spring 5.2以前

```java
public interface PlatformTransactionManager {

	TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException;

	void commit(TransactionStatus status) throws TransactionException;

	void rollback(TransactionStatus status) throws TransactionException;

}
```



#### [2]从 Spring 5.2开始

PlatformTransactionManager 接口本身没有变化，它继承了 TransactionManager

```java
public interface TransactionManager {
    
}
```



> TransactionManager接口中什么都没有，但是它还是有存在的意义——定义一个技术体系。



### ②技术体系

![./images](./images/img001.png)

我们现在要使用的事务管理器是org.springframework.jdbc.datasource.<span style="color:blue;font-weight:bold;">DataSourceTransactionManager</span>，将来整合 Mybatis 用的也是这个类。



DataSourceTransactionManager类中的主要方法：

- doBegin()：开启事务
- doSuspend()：挂起事务
- doResume()：恢复挂起的事务
- doCommit()：提交事务
- doRollback()：回滚事务



[上一节](verse01.html) [回目录](index.html) [下一节](verse03.html)