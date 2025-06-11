[[toc]]

# 第四节 基于XML的声明式事务

## 1、加入依赖

相比于基于注解的声明式事务，基于 XML 的声明式事务需要一个额外的依赖：

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.3.1</version>
        </dependency>
```



## 2、迁移代码

将上一个基于注解的 module 中的代码转移到新module。去掉 @Transactional 注解。



## 3、修改 Spring 配置文件

去掉 tx:annotation-driven 标签，然后加入下面的配置：

```xml
<aop:config>
    <!-- 配置切入点表达式，将事务功能定位到具体方法上 -->
    <aop:pointcut id="txPoincut" expression="execution(* *..*Service.*(..))"/>
    
    <!-- 将事务通知和切入点表达式关联起来 -->
    <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoincut"/>
    
</aop:config>
    
<!-- tx:advice标签：配置事务通知 -->
<!-- id属性：给事务通知标签设置唯一标识，便于引用 -->
<!-- transaction-manager属性：关联事务管理器 -->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
    
        <!-- tx:method标签：配置具体的事务方法 -->
        <!-- name属性：指定方法名，可以使用星号代表多个字符 -->
        <tx:method name="get*" read-only="true"/>
        <tx:method name="query*" read-only="true"/>
        <tx:method name="find*" read-only="true"/>
    
        <!-- read-only属性：设置只读属性 -->
        <!-- rollback-for属性：设置回滚的异常 -->
        <!-- no-rollback-for属性：设置不回滚的异常 -->
        <!-- isolation属性：设置事务的隔离级别 -->
        <!-- timeout属性：设置事务的超时属性 -->
        <!-- propagation属性：设置事务的传播行为 -->
        <tx:method name="save*" read-only="false" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
        <tx:method name="update*" read-only="false" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
        <tx:method name="delete*" read-only="false" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
    </tx:attributes>
</tx:advice>
```



## 4、注意

即使需要事务功能的目标方法已经被切入点表达式涵盖到了，但是如果没有给它配置事务属性，那么这个方法就还是没有事务。所以事务属性必须配置。



[上一节](verse03.html) [回目录](index.html)