[[toc]]

# 实验三 [重要]给bean的属性赋值：setter注入



## 1、给组件类添加一个属性

```java
public class HappyComponent {
    
    private String componentName;
    
    public String getComponentName() {
        return componentName;
    }
    
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
    
    public void doWork() {
        System.out.println("component do work ...");
    }
    
}
```



## 2、在配置时给属性指定值

通过property标签配置的属性值会通过setXxx()方法注入，大家可以通过debug方式验证一下

```xml
<!-- 实验三 [重要]给bean的属性赋值：setter注入 -->
<bean id="happyComponent3" class="com.atguigu.ioc.component.HappyComponent">
    
    <!-- property标签：通过组件类的setXxx()方法给组件对象设置属性 -->
    <!-- name属性：指定属性名（这个属性名是getXxx()、setXxx()方法定义的，和成员变量无关） -->
    <!-- value属性：指定属性值 -->
    <property name="componentName" value="veryHappy"/>
</bean>
```



## 3、测试

```java
@Test
public void testExperiment03() {
    
    HappyComponent happyComponent3 = (HappyComponent) iocContainer.getBean("happyComponent3");
    
    String componentName = happyComponent3.getComponentName();
    
    System.out.println("componentName = " + componentName);
    
}
```



[上一个实验](experiment02.html) [回目录](../verse03.html) [下一个实验](experiment04.html)