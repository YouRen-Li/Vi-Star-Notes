[[toc]]

# 第三节 给SQL语句传参

## 1、#{}方式

Mybatis会在运行过程中，把配置文件中的SQL语句里面的<span style="color:blue;font-weight:bold;">#{}</span>转换为“<span style="color:blue;font-weight:bold;">?</span>”占位符，发送给数据库执行。



配置文件中的SQL：

```xml
<delete id="deleteEmployeeById">
    delete from t_emp where emp_id=#{empId}
</delete>
```



实际执行的SQL：

```sql
delete from t_emp where emp_id=?
```



## 2、${}方式

将来会<span style="color:blue;font-weight:bold;">根据${}拼字符串</span>



### ①SQL语句

```xml
<select id="selectEmployeeByName" resultType="com.atguigu.mybatis.entity.Employee">
    select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_name like '%${empName}%'
</select>
```



### ②Mapper接口

注意：由于Mapper接口中方法名是作为SQL语句标签的id，不能重复，所以<span style="color:red;font-weight:bold;">Mapper接口中不能出现重名的方法</span>，<span style="color:blue;font-weight:bold;">不允许重载</span>！

```java
public interface EmployeeMapper {
    
    Employee selectEmployee(Integer empId);
    
    Employee selectEmployeeByName(@Param("empName") String empName);
    
    int insertEmployee(Employee employee);
    
    int deleteEmployee(Integer empId);
    
    int updateEmployee(Employee employee);
}
```



### ③junit测试

```java
@Test
public void testDollar() {
    
    EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
    
    Employee employee = employeeMapper.selectEmployeeByName("r");
    
    System.out.println("employee = " + employee);
}
```



### ④实际打印的SQL

```sql
select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_name like '%r%'
```



### ⑤应用场景举例

在SQL语句中，数据库表的表名不确定，需要外部动态传入，此时不能使用#{}，因为数据库不允许表名位置使用问号占位符，此时只能使用${}。<br/>

其他情况，<span style="color:red;font-weight:bold;">只要能用#{}肯定不用${}</span>，避免SQL注入。



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)