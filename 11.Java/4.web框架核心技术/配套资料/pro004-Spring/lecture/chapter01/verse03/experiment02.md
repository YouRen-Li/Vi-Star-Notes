[[toc]]

# 实验二 [重要]获取bean



## 1、方式一：根据id获取

由于 id 属性指定了 bean 的唯一标识，所以根据 bean 标签的 id 属性可以精确获取到一个组件对象。上个实验中我们使用的就是这种方式。



## 2、方式二：根据类型获取

### ①指定类型的 bean 唯一

```java
@Test
public void testExperiment02() {
    
    HappyComponent component = iocContainer.getBean(HappyComponent.class);
    
    component.doWork();
    
}
```



### ②指令类型的 bean 不唯一

相同类型的 bean 在IOC容器中一共配置了两个：

```xml
<!-- 实验一 [重要]创建bean -->
<bean id="happyComponent" class="com.atguigu.ioc.component.HappyComponent"/>

<!-- 实验二 [重要]获取bean -->
<bean id="happyComponent2" class="com.atguigu.ioc.component.HappyComponent"/>
```



根据类型获取时会抛出异常：

> org.springframework.beans.factory.<span style="color:blue;font-weight:bold;">NoUniqueBeanDefinitionException</span>: No qualifying bean of type 'com.atguigu.ioc.component.HappyComponent' available: expected single matching bean but found 2: happyComponent,happyComponent2



### ③思考

如果组件类实现了接口，根据接口类型可以获取 bean 吗？

> 可以，前提是bean唯一

如果一个接口有多个实现类，这些实现类都配置了 bean，根据接口类型可以获取 bean 吗？

> 不行，因为bean不唯一



### ④结论

根据类型来获取bean时，在满足bean唯一性的前提下，其实只是看：『对象 <span style="color:blue;font-weight:bold;">instanceof</span> 指定的类型』的返回结果，只要返回的是true就可以认定为和类型匹配，能够获取到。



[上一个实验](experiment01.html) [回目录](../verse03.html) [下一个实验](experiment03.html)