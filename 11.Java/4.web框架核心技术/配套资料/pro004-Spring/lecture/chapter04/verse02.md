[[toc]]



# 第二节 整合junit5



## 1、导入依赖

在原有环境基础上增加如下依赖：

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.1</version>
</dependency>
```



## 2、创建测试类

- @ExtendWith(SpringExtension.class) 表示使用 Spring 提供的扩展功能。
- @ContextConfiguration(value = {"classpath:spring-context.xml"}) 还是用来指定 Spring 配置文件位置，和整合 junit4 一样。

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = {"classpath:spring-context.xml"})
public class Junit5IntegrationTest {
    
    @Autowired
    private EmpDao empDao;
    
    @Test
    public void testJunit5() {
        System.out.println("empDao = " + empDao);
    }
    
}
```



## 3、使用复合注解

@SpringJUnitConfig 注解综合了前面两个注解的功能，此时指定 Spring 配置文件位置即可。但是注意此时需要使用 locations 属性，不是 value 属性了。

```java
@SpringJUnitConfig(locations = {"classpath:spring-context.xml"})
public class Junit5IntegrationTest {
    
    @Autowired
    private EmpDao empDao;
    
    @Test
    public void testJunit5() {
        System.out.println("empDao = " + empDao);
    }
    
}
```



[上一节](verse01.html) [回目录](index.html)
