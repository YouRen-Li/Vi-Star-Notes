[[toc]]

# 实验一 [重要]创建bean



## 1、实验目标和思路

### ①目标

由 Spring 的 IOC 容器创建类的对象。



### ②思路

![images](../images/img006.png)



## 2、创建Maven Module

```xml
<dependencies>
    <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.1</version>
    </dependency>
    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

![images](../images/img005.png)



## 3、创建组件类

```java
package com.atguigu.ioc.component;
    
public class HappyComponent {
    
    public void doWork() {
        System.out.println("component do work ...");
    }
    
}
```



## 4、创建 Spring 配置文件

![images](../images/img007.png)



![images](../images/img008.png)



## 5、配置组件

```xml
<!-- 实验一 [重要]创建bean -->
<bean id="happyComponent" class="com.atguigu.ioc.component.HappyComponent"/>
```

- bean标签：通过配置bean标签告诉IOC容器需要创建对象的组件是什么
- id属性：bean的唯一标识
- class属性：组件类的全类名



## 6、创建测试类

```java
public class IOCTest {
    
    // 创建 IOC 容器对象，为便于其他实验方法使用声明为成员变量
    private ApplicationContext iocContainer = new ClassPathXmlApplicationContext("applicationContext.xml");
    
    @Test
    public void testExperiment01() {
    
        // 从 IOC 容器对象中获取bean，也就是组件对象
        HappyComponent happyComponent = (HappyComponent) iocContainer.getBean("happyComponent");
    
        happyComponent.doWork();
    
    }
    
}
```



## 7、无参构造器

Spring 底层默认通过反射技术调用组件类的无参构造器来创建组件对象，这一点需要注意。如果在需要无参构造器时，没有无参构造器，则会抛出下面的异常：

> org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'happyComponent1' defined in class path resource [applicationContext.xml]: Instantiation of bean failed; 
>
> nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.atguigu.ioc.component.HappyComponent]: No default constructor found; 
>
> nested exception is java.lang.NoSuchMethodException: com.atguigu.ioc.component.HappyComponent.<init>()



所以对一个JavaBean来说，<span style="color:blue;font-weight:bold;">无参构造器</span>和<span style="color:blue;font-weight:bold;">属性的getXxx()、setXxx()方法</span>是<span style="color:blue;font-weight:bold;">必须存在</span>的，特别是在框架中。



## 8、用IOC容器创建对象和自己建区别

![images](../images/img013.png)

在Spring环境下能够享受到的所有福利，都必须通过 IOC 容器附加到组件类上，所以随着我们在 Spring 中学习的功能越来越多，IOC 容器创建的组件类的对象就会比自己 new 的对象强大的越来越多。



[回目录](../verse03.html) [下一个实验](experiment02.html)
