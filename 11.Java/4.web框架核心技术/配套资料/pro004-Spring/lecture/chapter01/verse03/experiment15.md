[[toc]]

# 实验十五 bean的作用域

## 1、概念

在Spring中可以通过配置bean标签的scope属性来指定bean的作用域范围，各取值含义参加下表：

| 取值      | 含义                                    | 创建对象的时机  |
| --------- | --------------------------------------- | --------------- |
| singleton | 在IOC容器中，这个bean的对象始终为单实例 | IOC容器初始化时 |
| prototype | 这个bean在IOC容器中有多个实例           | 获取bean时      |



如果是在WebApplicationContext环境下还会有另外两个作用域（但不常用）：

| 取值    | 含义                 |
| ------- | -------------------- |
| request | 在一个请求范围内有效 |
| session | 在一个会话范围内有效 |



## 2、配置

```xml
<!-- 实验十五 bean的作用域 -->
<!-- scope属性：取值singleton（默认值），bean在IOC容器中只有一个实例，IOC容器初始化时创建对象 -->
<!-- scope属性：取值prototype，bean在IOC容器中可以有多个实例，getBean()时创建对象 -->
<bean id="happyMachine4" scope="prototype" class="com.atguigu.ioc.component.HappyMachine">
    <property name="machineName" value="iceCreamMachine"/>
</bean>
```





## 3、测试

```java
@Test
public void testExperiment15() {
    HappyMachine happyMachine01 = (HappyMachine) iocContainer.getBean("happyMachine4");
    HappyMachine happyMachine02 = (HappyMachine) iocContainer.getBean("happyMachine4");
    
    System.out.println(happyMachine01 == happyMachine02);
    
    System.out.println("happyMachine01.hashCode() = " + happyMachine01.hashCode());
    System.out.println("happyMachine02.hashCode() = " + happyMachine02.hashCode());
}
```





[上一个实验](experiment14.html) [回目录](../verse03.html) [下一个实验](experiment16.html)
