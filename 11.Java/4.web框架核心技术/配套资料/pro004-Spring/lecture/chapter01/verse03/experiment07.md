[[toc]]

# 实验七 给bean的属性赋值：级联属性赋值

## 1、配置关联对象的 bean

```xml
<bean id="happyMachine2" class="com.atguigu.ioc.component.HappyMachine"/>
```



## 2、装配关联对象并赋值级联属性

关联对象：happyMachine

级联属性：happyMachine.machineName

```xml
<!-- 实验七 给bean的属性赋值：级联属性赋值 -->
<bean id="happyComponent6" class="com.atguigu.ioc.component.HappyComponent">
    <!-- 装配关联对象 -->
    <property name="happyMachine" ref="happyMachine2"/>
    <!-- 对HappyComponent来说，happyMachine的machineName属性就是级联属性 -->
    <property name="happyMachine.machineName" value="cascadeValue"/>
</bean>
```



## 3、测试

```java
@Test
public void testExperiment07() {
    
    HappyComponent happyComponent6 = (HappyComponent) iocContainer.getBean("happyComponent6");
    
    String machineName = happyComponent6.getHappyMachine().getMachineName();
    
    System.out.println("machineName = " + machineName);

}
```



[上一个实验](experiment06.html) [回目录](../verse03.html) [下一个实验](experiment08.html)
