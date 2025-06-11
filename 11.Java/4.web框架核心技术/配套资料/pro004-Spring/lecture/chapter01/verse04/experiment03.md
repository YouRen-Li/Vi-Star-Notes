[[toc]]

# 第三个实验 完全注解开发

体验完全注解开发，是为了给将来学习SpringBoot打基础。因为在SpringBoot中，就是完全舍弃XML配置文件，全面使用注解来完成主要的配置。



## 1、使用配置类取代配置文件

### ①创建配置类

使用@Configuration注解将一个普通的类标记为Spring的配置类。

```java
package com.atguigu.ioc.configuration;
    
import org.springframework.context.annotation.Configuration;
    
@Configuration
public class MyConfiguration {
}
```



### ②根据配置类创建IOC容器对象

```java
// ClassPathXmlApplicationContext根据XML配置文件创建IOC容器对象
private ApplicationContext iocContainer = new ClassPathXmlApplicationContext("applicationContext.xml");

// AnnotationConfigApplicationContext根据配置类创建IOC容器对象
private ApplicationContext iocContainerAnnotation = new AnnotationConfigApplicationContext(MyConfiguration.class);
```



## 2、在配置类中配置bean

使用@Bean注解

```java
@Configuration
public class MyConfiguration {
    
    // @Bean注解相当于XML配置文件中的bean标签
    // @Bean注解标记的方法的返回值会被放入IOC容器
    @Bean
    public CommonComponent getComponent() {
    
        CommonComponent commonComponent = new CommonComponent();
    
        commonComponent.setComponentName("created by annotation config");
    
        return commonComponent;
    }
    
}
```



## 3、在配置类中配置自动扫描的包

```java
@Configuration
@ComponentScan("com.atguigu.ioc.component")
public class MyConfiguration {
    ……
```



[上一个实验](experiment02.html) [回目录](../verse04.html) [下一个实验](experiment04.html)
