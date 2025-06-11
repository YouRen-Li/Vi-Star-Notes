[[toc]]

# 实验十三 集合类型的bean

## 1、配置

```xml
<!-- 实验十一 给bean的属性赋值：集合属性 -->
<util:list id="machineList">
    <bean class="com.atguigu.ioc.component.HappyMachine">
        <property name="machineName" value="machineOne"/>
    </bean>
    <bean class="com.atguigu.ioc.component.HappyMachine">
        <property name="machineName" value="machineTwo"/>
    </bean>
    <bean class="com.atguigu.ioc.component.HappyMachine">
        <property name="machineName" value="machineThree"/>
    </bean>
</util:list>
```



## 2、测试

```java
@Test
public void testExperiment11() {
    List<HappyMachine> machineList = (List<HappyMachine>) iocContainer.getBean("machineList");
    for (HappyMachine happyMachine : machineList) {
        System.out.println("happyMachine = " + happyMachine);
    }
}
```



[上一个实验](experiment10.html) [回目录](../verse03.html) [下一个实验](experiment12.html)