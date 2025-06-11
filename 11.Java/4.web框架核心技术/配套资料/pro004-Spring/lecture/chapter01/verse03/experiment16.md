[[toc]]

# 实验十六 bean的生命周期

## 1、bean的生命周期清单

- bean对象创建（调用无参构造器）
- 给bean对象设置属性
- bean对象初始化之前操作（由bean的后置处理器负责）
- bean对象初始化（需在配置bean时指定初始化方法）
- bean对象初始化之后操作（由bean的后置处理器负责）
- bean对象就绪可以使用
- bean对象销毁（需在配置bean时指定销毁方法）
- IOC容器关闭



## 2、指定bean的初始化方法和销毁方法

### ①创建两个方法作为初始化和销毁方法

用com.atguigu.ioc.component.HappyComponent类测试：

```java
public void happyInitMethod() {
    System.out.println("HappyComponent初始化");
}
    
public void happyDestroyMethod() {
    System.out.println("HappyComponent销毁");
}
```



### ②配置bean时指定初始化和销毁方法

```xml
<!-- 实验十六 bean的生命周期 -->
<!-- 使用init-method属性指定初始化方法 -->
<!-- 使用destroy-method属性指定销毁方法 -->
<bean id="happyComponent"
      class="com.atguigu.ioc.component.HappyComponent"
      init-method="happyInitMethod"
      destroy-method="happyDestroyMethod"
>
    <property name="happyName" value="uuu"/>
</bean>
```



## 3、bean的后置处理器

### ①创建后置处理器类

```java
package com.atguigu.ioc.process;
    
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
    
// 声明一个自定义的bean后置处理器
// 注意：bean后置处理器不是单独针对某一个bean生效，而是针对IOC容器中所有bean都会执行
public class MyHappyBeanProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    
        System.out.println("☆☆☆" + beanName + " = " + bean);
    
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    
        System.out.println("★★★" + beanName + " = " + bean);
    
        return bean;
    }
}
```



### ②把bean的后置处理器放入IOC容器

```xml
<!-- bean的后置处理器要放入IOC容器才能生效 -->
<bean id="myHappyBeanProcessor" class="com.atguigu.ioc.process.MyHappyBeanProcessor"/>
```



### ③执行效果示例

> HappyComponent创建对象
> HappyComponent要设置属性了
> ☆☆☆happyComponent = com.atguigu.ioc.component.HappyComponent@ca263c2
> HappyComponent初始化
> ★★★happyComponent = com.atguigu.ioc.component.HappyComponent@ca263c2
> HappyComponent销毁



[上一个实验](experiment15.html) [回目录](../verse03.html)