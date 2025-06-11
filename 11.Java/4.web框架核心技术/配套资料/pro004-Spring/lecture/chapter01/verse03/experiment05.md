[[toc]]



# 实验五 [重要]给bean的属性赋值：内部bean

## 1、重新配置原组件

在bean里面配置的bean就是内部bean，内部bean只能在当前bean内部使用，在其他地方不能使用。

```xml
<!-- 实验五 [重要]给bean的属性赋值：内部bean -->
<bean id="happyComponent5" class="com.atguigu.ioc.component.HappyComponent">
    <property name="happyMachine">
        <!-- 在一个 bean 中再声明一个 bean 就是内部 bean -->
        <!-- 内部 bean 可以直接用于给属性赋值，可以省略 id 属性 -->
        <bean class="com.atguigu.ioc.component.HappyMachine">
            <property name="machineName" value="makeHappy"/>
        </bean>
    </property>
</bean>
```



## 2、测试

```java
@Test
public void testExperiment04() {
    HappyComponent happyComponent4 = (HappyComponent) iocContainer.getBean("happyComponent4");
    
    HappyMachine happyMachine = happyComponent4.getHappyMachine();
    
    String machineName = happyMachine.getMachineName();
    
    System.out.println("machineName = " + machineName);
}
```





[上一个实验](experiment04.html) [回目录](../verse03.html) [下一个实验](experiment06.html)