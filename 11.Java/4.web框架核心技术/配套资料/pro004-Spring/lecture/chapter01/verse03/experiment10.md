[[toc]]

# 实验十 给bean的属性赋值：使用p名称空间

## 1、配置

使用 p 名称空间的方式可以省略子标签 property，将组件属性的设置作为 bean 标签的属性来完成。

```xml
<!-- 实验十 给bean的属性赋值：使用p名称空间 -->
<bean id="happyMachine3"
      class="com.atguigu.ioc.component.HappyMachine"
      p:machineName="goodMachine"
/>
```



使用 p 名称空间需要导入相关的 XML 约束，在 IDEA 的协助下导入即可：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
```



具体操作时，输入p:稍微等一下，等IDEA弹出下面的提示：

![images](../images/img014.png)

按Alt+Enter即可导入。



## 2、测试

```java
@Test
public void testExperiment10() {
    HappyMachine happyMachine3 = (HappyMachine) iocContainer.getBean("happyMachine3");
    
    String machineName = happyMachine3.getMachineName();
    
    System.out.println("machineName = " + machineName);
}
```



[上一个实验](experiment09.html) [回目录](../verse03.html) [下一个实验](experiment11.html)