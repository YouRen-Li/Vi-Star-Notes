[[toc]]

# 实验十二 自动装配

## 1、声明组件类

其中HappyController需要用到HappyService。所谓自动装配就是一个组件需要其他组件时，由 IOC 容器负责找到那个需要的组件，并装配进去。

```java
public class HappyController {
        
    private HappyService happyService;
    
    public HappyService getHappyService() {
        return happyService;
    }
    
    public void setHappyService(HappyService happyService) {
        this.happyService = happyService;
    }
}
```



```java
public class HappyService {
}
```



## 2、配置

```xml
<!-- 实验十二 自动装配 -->
<bean id="happyService3" class="com.atguigu.ioc.component.HappyService"/>
<bean id="happyService2" class="com.atguigu.ioc.component.HappyService"/>

<!-- 使用bean标签的autowire属性设置自动装配效果 -->
<!-- byType表示根据类型进行装配，此时如果类型匹配的bean不止一个，那么会抛NoUniqueBeanDefinitionException -->
<!-- byName表示根据bean的id进行匹配。而bean的id是根据需要装配组件的属性的属性名来确定的 -->
<bean id="happyController"
      class="com.atguigu.ioc.component.HappyController"
      autowire="byName"
>
    <!-- 手动装配：在property标签中使用ref属性明确指定要装配的bean -->
    <!--<property name="happyService" ref="happyService"/>-->
</bean>
```



## 3、测试

```java
@Test
public void testExperiment12() {
    HappyController happyController = iocContainer.getBean(HappyController.class);
    
    HappyService happyService = happyController.getHappyService();
    
    System.out.println("happyService = " + happyService);
}
```



[上一个实验](experiment11.html) [回目录](../verse03.html) [下一个实验](experiment13.html)