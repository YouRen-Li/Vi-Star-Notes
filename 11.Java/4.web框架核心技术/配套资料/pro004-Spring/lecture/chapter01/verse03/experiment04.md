[[toc]]



# 实验四 [重要]给bean的属性赋值：引用外部已声明的bean



## 1、声明新的组件类

```java
public class HappyMachine {
    
    private String machineName;
    
    public String getMachineName() {
        return machineName;
    }
    
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
}
```



## 2、原组件引用新组件

![images](../images/img009.png)



## 3、配置新组件的 bean

```xml
<bean id="happyMachine" class="com.atguigu.ioc.component.HappyMachine">
    <property name="machineName" value="makeHappy"
</bean>
```



## 4、在原组件的 bean 中引用新组件的 bean

```xml
<bean id="happyComponent4" class="com.atguigu.ioc.component.HappyComponent">
    <!-- ref 属性：通过 bean 的 id 引用另一个 bean -->
    <property name="happyMachine" ref="happyMachine"/>
</bean>
```



这个操作在 IDEA 中有提示：

![images](../images/img010.png)



## 5、测试

```java
@Test
public void testExperiment04() {
    HappyComponent happyComponent4 = (HappyComponent) iocContainer.getBean("happyComponent4");
    
    HappyMachine happyMachine = happyComponent4.getHappyMachine();
    
    String machineName = happyMachine.getMachineName();
    
    System.out.println("machineName = " + machineName);
}
```



## 6、易错点

> 如果错把ref属性写成了value属性，会抛出异常：
> Caused by: java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'com.atguigu.ioc.component.HappyMachine' for property 'happyMachine': no matching editors or conversion strategy found
> 意思是不能把String类型转换成我们要的HappyMachine类型
> 说明我们使用value属性时，Spring只把这个属性看做一个普通的字符串，不会认为这是一个bean的id，更不会根据它去找到bean来赋值



[上一个实验](experiment03.html) [回目录](../verse03.html) [下一个实验](experiment05.html)