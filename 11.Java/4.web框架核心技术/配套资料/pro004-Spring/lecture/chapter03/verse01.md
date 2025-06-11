[[toc]]

# 第一节 JDBCTemplate

## 1、简介

为了在特定领域帮助我们简化代码，Spring 封装了很多 『Template』形式的模板类。例如：RedisTemplate、RestTemplate 等等，包括我们今天要学习的 JDBCTemplate。



## 2、准备工作

### ①加入依赖

```xml
<dependencies>

    <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 持久化层支持jar包 -->
    <!-- Spring 在执行持久化层操作、与持久化层技术进行整合过程中，需要使用orm、jdbc、tx三个jar包 -->
    <!-- 导入 orm 包就可以通过 Maven 的依赖传递性把其他两个也导入 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 测试相关 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.3</version>
    </dependency>
    <!-- 数据源 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.0.31</version>
    </dependency>

</dependencies>
```



### ②jdbc.properties

```properties
atguigu.url=jdbc:mysql://192.168.198.100:3306/mybatis-example
atguigu.driver=com.mysql.jdbc.Driver
atguigu.username=root
atguigu.password=atguigu
```



### ③Spring 配置文件

#### [1]配置数据源

```xml
<!-- 导入外部属性文件 -->
<context:property-placeholder location="classpath:jdbc.properties" />
    
<!-- 配置数据源 -->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="url" value="${atguigu.url}"/>
    <property name="driverClassName" value="${atguigu.driver}"/>
    <property name="username" value="${atguigu.username}"/>
    <property name="password" value="${atguigu.password}"/>
</bean>
```



#### [2]配置 JDBCTemplate

```xml
<!-- 配置 JdbcTemplate -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        
    <!-- 装配数据源 -->
    <property name="dataSource" ref="druidDataSource"/>
        
</bean>
```



#### [3]在测试类装配 JdbcTemplate

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring-context.xml"})
public class JDBCTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
        
    @Test
    public void testJdbcTemplateUpdate() {
        
    }
    
    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
    
        System.out.println("connection = " + connection);
    }
    
}
```



## 3、基本用法

### ①增删改操作

```java
@Test
public void testJdbcTemplateUpdate() {
    
    // 1.编写 SQL 语句。需要传参的地方写问号占位符
    String sql = "update t_emp set emp_salary=? where emp_id=?";
    
    // 2.调用 jdbcTemplate 的 update() 方法执行 update 语句
    int count = jdbcTemplate.update(sql, 999.99, 3);
    
    System.out.println("count = " + count);
    
}
```



### ②查询：返回单个简单类型

```java
@Test
public void testJdbcTemplateQueryForSingleValue() {
    
    // 1.编写 SQL 语句
    String sql = "select emp_name from t_emp where emp_id=?";
    
    // 2.调用 jdbcTemplate 的方法执行查询
    String empName = jdbcTemplate.queryForObject(sql, String.class, 6);
    
    System.out.println("empName = " + empName);
    
}
```



### ③查询：查询实体类类型

#### [1]封装实体类类型

```java
public class Emp {
    
    private Integer empId;
    private String empName;
    private Double empSalary;
    ……
```



#### [2]借助 RowMapper 完成查询

```java
@Test
public void testJdbcTemplateQueryForEntity() {
    
    // 1.编写 SQL 语句
    String sql = "select emp_id,emp_name,emp_salary from t_emp where emp_id=?";
    
    // 2.准备 RowMapper 对象
    RowMapper<Emp> rowMapper = new BeanPropertyRowMapper<>(Emp.class);
    
    // 3.调用 jdbcTemplate 的方法执行查询
    Emp emp = jdbcTemplate.queryForObject(sql, rowMapper, 7);
    
    System.out.println("emp = " + emp);
    
}
```



[回目录](index.html) [下一节](verse02.html)