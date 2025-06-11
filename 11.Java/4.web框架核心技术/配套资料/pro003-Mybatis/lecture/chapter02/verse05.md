[[toc]]

# 第五节 数据输出



## 1、返回单个简单类型数据

### ①Mapper接口中的抽象方法

```java
int selectEmpCount();
```



### ②SQL语句

```java
    <select id="selectEmpCount" resultType="int">
        select count(*) from t_emp
    </select>
```



### ③junit测试

```java
    @Test
    public void testEmpCount() {
    
        EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
    
        int count = employeeMapper.selectEmpCount();
    
        System.out.println("count = " + count);
    }
```



## 2、返回实体类对象

### ①Mapper接口的抽象方法

```java
Employee selectEmployee(Integer empId);
```



### ②SQL语句

```xml
<!-- 编写具体的SQL语句，使用id属性唯一的标记一条SQL语句 -->
<!-- resultType属性：指定封装查询结果的Java实体类的全类名 -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">
    <!-- Mybatis负责把SQL语句中的#{}部分替换成“?”占位符 -->
    <!-- 给每一个字段设置一个别名，让别名和Java实体类中属性名一致 -->
    select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_id=#{maomi}
</select>
```

通过给数据库表字段加别名，让查询结果的每一列都和Java实体类中属性对应起来。



### ③增加全局配置自动识别对应关系

做了下面的配置，select语句中可以不给字段设置别名

```xml
<!-- 在全局范围内对Mybatis进行配置 -->
<settings>
    <!-- 具体配置 -->
    <!-- 从org.apache.ibatis.session.Configuration类中可以查看能使用的配置项 -->
    <!-- 将mapUnderscoreToCamelCase属性配置为true，表示开启自动映射驼峰式命名规则 -->
    <!-- 规则要求数据库表字段命名方式：单词_单词 -->
    <!-- 规则要求Java实体类属性名命名方式：首字母小写的驼峰式命名 -->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```



## 3、返回Map类型

适用于SQL查询返回的各个字段综合起来并不和任何一个现有的实体类对应，没法封装到实体类对象中。<span style="color:red;font-weight:bold;">能够封装成实体类类型的，就不使用Map类型</span>。



### ①Mapper接口的抽象方法

```java
Map<String,Object> selectEmpNameAndMaxSalary();
```



### ②SQL语句

```xml
<!-- Map<String,Object> selectEmpNameAndMaxSalary(); -->
<!-- 返回工资最高的员工的姓名和他的工资 -->
<select id="selectEmpNameAndMaxSalary" resultType="map">
        SELECT
            emp_name 员工姓名,
            emp_salary 员工工资,
            (SELECT AVG(emp_salary) FROM t_emp) 部门平均工资
        FROM t_emp WHERE emp_salary=(
            SELECT MAX(emp_salary) FROM t_emp
        )
</select>
```



### ③junit测试

```java
    @Test
    public void testQueryEmpNameAndSalary() {
    
        EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
    
        Map<String, Object> resultMap = employeeMapper.selectEmpNameAndMaxSalary();
    
        Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();
    
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }
```



## 4、返回List类型

查询结果返回多个实体类对象，希望把多个实体类对象放在List集合中返回。此时不需要任何特殊处理，在resultType属性中还是设置实体类类型即可。



### ①Mapper接口中抽象方法

```java
List<Employee> selectAll();
```



### ②SQL语句

```xml
    <!-- List<Employee> selectAll(); -->
    <select id="selectAll" resultType="com.atguigu.mybatis.entity.Employee">
        select emp_id empId,emp_name empName,emp_salary empSalary
        from t_emp
    </select>
```



### ③junit测试

```java
    @Test
    public void testSelectAll() {
    
        EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
    
        List<Employee> employeeList = employeeMapper.selectAll();
    
        for (Employee employee : employeeList) {
            System.out.println("employee = " + employee);
        }
    
    }
```



## 5、返回自增主键

### ①使用场景

例如：保存订单信息。需要保存Order对象和List&lt;OrderItem&gt;。其中，OrderItem对应的数据库表，包含一个外键，指向Order对应表的主键。<br/>

在保存List&lt;OrderItem&gt;的时候，需要使用下面的SQL：

```sql
insert into t_order_item(item_name,item_price,item_count,order_id) values(...)
```

这里需要用到的order_id，是在保存Order对象时，数据库表以自增方式产生的，需要特殊办法拿到这个自增的主键值。至于，为什么不能通过查询最大主键的方式解决这个问题，参考下图：

![./images](./images/img008.png)



### ②在Mapper配置文件中设置方式

#### [1]Mapper接口中的抽象方法

```java
int insertEmployee(Employee employee);
```



#### [2]SQL语句

```xml
<!-- int insertEmployee(Employee employee); -->
<!-- useGeneratedKeys属性字面意思就是“使用生成的主键” -->
<!-- keyProperty属性可以指定主键在实体类对象中对应的属性名，Mybatis会将拿到的主键值存入这个属性 -->
<insert id="insertEmployee" useGeneratedKeys="true" keyProperty="empId">
    insert into t_emp(emp_name,emp_salary)
    values(#{empName},#{empSalary})
</insert>
```



### [3]junit测试

```java
@Test
public void testSaveEmp() {
    
    EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
    
    Employee employee = new Employee();
        
    employee.setEmpName("john");
    employee.setEmpSalary(666.66);
    
    employeeMapper.insertEmployee(employee);
    
    System.out.println("employee.getEmpId() = " + employee.getEmpId());
    
}
```



### ④注意

Mybatis是将自增主键的值设置到实体类对象中，而<span style="color:blue;font-weight:bold;">不是以Mapper接口方法返回值</span>的形式返回。



### ⑤不支持自增主键的数据库

而对于不支持自增型主键的数据库（例如 Oracle），则可以使用 selectKey 子元素：selectKey  元素将会首先运行，id  会被设置，然后插入语句会被调用

```xml
<insert id="insertEmployee" 
		parameterType="com.atguigu.mybatis.beans.Employee"  
			databaseId="oracle">
		<selectKey order="BEFORE" keyProperty="id" 
                                       resultType="integer">
			select employee_seq.nextval from dual 
		</selectKey>	
		insert into orcl_employee(id,last_name,email,gender) values(#{id},#{lastName},#{email},#{gender})
</insert>
```

或者是

```xml
<insert id="insertEmployee" 
		parameterType="com.atguigu.mybatis.beans.Employee"  
			databaseId="oracle">
		<selectKey order="AFTER" keyProperty="id" 
                                         resultType="integer">
			select employee_seq.currval from dual 
		</selectKey>	
	insert into orcl_employee(id,last_name,email,gender) values(employee_seq.nextval,#{lastName},#{email},#{gender})
</insert>
```



## 6、数据库表字段和实体类属性对应关系

### ①别名

将字段的别名设置成和实体类属性一致。

```xml
<!-- 编写具体的SQL语句，使用id属性唯一的标记一条SQL语句 -->
<!-- resultType属性：指定封装查询结果的Java实体类的全类名 -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">
    <!-- Mybatis负责把SQL语句中的#{}部分替换成“?”占位符 -->
    <!-- 给每一个字段设置一个别名，让别名和Java实体类中属性名一致 -->
    select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_id=#{maomi}
</select>
```

> 关于实体类属性的约定：
>
> getXxx()方法、setXxx()方法把方法名中的get或set去掉，首字母小写。



### ②全局配置自动识别驼峰式命名规则

在Mybatis全局配置文件加入如下配置：

```xml
<!-- 使用settings对Mybatis全局进行设置 -->
<settings>
    <!-- 将xxx_xxx这样的列名自动映射到xxXxx这样驼峰式命名的属性名 -->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

SQL语句中可以不使用别名

```xml
<!-- Employee selectEmployee(Integer empId); -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">
    select emp_id,emp_name,emp_salary from t_emp where emp_id=#{empId}
</select>
```



### ③使用resultMap

使用resultMap标签定义对应关系，再在后面的SQL语句中引用这个对应关系

```xml
<!-- 专门声明一个resultMap设定column到property之间的对应关系 -->
<resultMap id="selectEmployeeByRMResultMap" type="com.atguigu.mybatis.entity.Employee">
    
    <!-- 使用id标签设置主键列和主键属性之间的对应关系 -->
    <!-- column属性用于指定字段名；property属性用于指定Java实体类属性名 -->
    <id column="emp_id" property="empId"/>
    
    <!-- 使用result标签设置普通字段和Java实体类属性之间的关系 -->
    <result column="emp_name" property="empName"/>
    <result column="emp_salary" property="empSalary"/>
</resultMap>
    
<!-- Employee selectEmployeeByRM(Integer empId); -->
<select id="selectEmployeeByRM" resultMap="selectEmployeeByRMResultMap">
    select emp_id,emp_name,emp_salary from t_emp where emp_id=#{empId}
</select>
```



[上一节](verse04.html) [回目录](index.html)