[[toc]]

# 第四个实验 整合junit4

## 1、整合的好处

- 好处1：不需要自己创建IOC容器对象了
- 好处2：任何需要的bean都可以在测试类中直接享受自动装配



## 2、操作

### ①加入依赖

```xml
<!-- Spring的测试包 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.1</version>
</dependency>
```



### ②创建测试类

```java
// junit的@RunWith注解：指定Spring为Junit提供的运行器
@RunWith(SpringJUnit4ClassRunner.class)

// Spring的@ContextConfiguration指定Spring配置文件的位置
@ContextConfiguration(value = {"classpath:applicationContext.xml"})
public class JunitIntegrationSpring {
    
    @Autowired
    private SoldierController soldierController;
    
    @Test
    public void testIntegration() {
        System.out.println("soldierController = " + soldierController);
    }
    
}
```



[上一个实验](experiment03.html) [回目录](../verse04.html)