[[toc]]

# 实验一 准备工作

## 1、加入依赖

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



## 2、外部属性文件

```properties
atguigu.url=jdbc:mysql://192.168.198.100:3306/mybatis-example
atguigu.driver=com.mysql.jdbc.Driver
atguigu.username=root
atguigu.password=atguigu
```



## 3、Spring 配置文件

```xml
<!-- 配置自动扫描的包 -->
<context:component-scan base-package="com.atguigu.tx"/>

<!-- 导入外部属性文件 -->
<context:property-placeholder location="classpath:jdbc.properties" />
    
<!-- 配置数据源 -->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="url" value="${atguigu.url}"/>
    <property name="driverClassName" value="${atguigu.driver}"/>
    <property name="username" value="${atguigu.username}"/>
    <property name="password" value="${atguigu.password}"/>
</bean>
    
<!-- 配置 JdbcTemplate -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    
    <!-- 装配数据源 -->
    <property name="dataSource" ref="druidDataSource"/>
    
</bean>
```



## 4、测试类

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring-context.xml"})
public class JDBCTest {
   
}
```



## 5、创建组件

### ①EmpDao

```java
@Repository
public class EmpDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
        
    public void updateEmpNameById(Integer empId, String empName) {
        String sql = "update t_emp set emp_name=? where emp_id=?";
        jdbcTemplate.update(sql, empName, empId);
    }
        
    public void updateEmpSalaryById(Integer empId, Double salary) {
        String sql = "update t_emp set emp_salary=? where emp_id=?";
        jdbcTemplate.update(sql, salary, empId);
    }
        
    public String selectEmpNameById(Integer empId) {
        String sql = "select emp_name from t_emp where emp_id=?";
    
        String empName = jdbcTemplate.queryForObject(sql, String.class, empId);
    
        return empName;
    }
    
}
```

EmpDao 准备好之后最好测试一下，确认代码正确。养成随写随测的好习惯。



### ②EmpService

在三层结构中，事务通常都是加到业务逻辑层，针对Service类使用事务。

```java
@Service
public class EmpService {
    
    @Autowired
    private EmpDao empDao;
    
    // 为了便于核对数据库操作结果，不要修改同一条记录
    public void updateTwice(
            // 修改员工姓名的一组参数
            Integer empId4EditName, String newName,

            // 修改员工工资的一组参数
            Integer empId4EditSalary, Double newSalary
            ) {
    
        // 为了测试事务是否生效，执行两个数据库操作，看它们是否会在某一个失败时一起回滚
        empDao.updateEmpNameById(empId4EditName, newName);
    
        empDao.updateEmpSalaryById(empId4EditSalary, newSalary);
    
    }
    
}
```



[回目录](../verse03.html) [下一个实验](experiment02.html)
