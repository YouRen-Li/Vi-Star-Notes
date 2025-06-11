[[toc]]

# 第二节 一级缓存

## 1、代码验证一级缓存

```java
@Test
public void testFirstLevelCache() {
    
    EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
    
    // 1.第一次查询
    Employee employee1 = mapper.selectEmployeeById(2);
    
    System.out.println("employee1 = " + employee1);
    
    // 2.第二次查询
    Employee employee2 = mapper.selectEmployeeById(2);
    
    System.out.println("employee2 = " + employee2);
    
    // 3.经过验证发现，两次查询返回的其实是同一个对象
    System.out.println("(employee2 == employee1) = " + (employee2 == employee1));
    System.out.println("employee1.equals(employee2) = " + employee1.equals(employee2));
    System.out.println("employee1.hashCode() = " + employee1.hashCode());
    System.out.println("employee2.hashCode() = " + employee2.hashCode());
    
}
```



打印结果：

```java
DEBUG 12-01 09:14:48,760 ==>  Preparing: select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp where emp_id=?   (BaseJdbcLogger.java:145) 
DEBUG 12-01 09:14:48,804 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 12-01 09:14:48,830 <==      Total: 1  (BaseJdbcLogger.java:145) 
employee1 = Employee{empId=2, empName='AAAAAA', empSalary=6666.66, empAge=20, empGender='male'}
employee2 = Employee{empId=2, empName='AAAAAA', empSalary=6666.66, empAge=20, empGender='male'}
(employee2 == employee1) = true
employee1.equals(employee2) = true
employee1.hashCode() = 1131645570
employee2.hashCode() = 1131645570
```

一共只打印了一条SQL语句，两个变量指向同一个对象。



## 2、一级缓存失效的情况

- 不是同一个SqlSession
- 同一个SqlSession但是查询条件发生了变化
- 同一个SqlSession两次查询期间执行了任何一次增删改操作
- 同一个SqlSession两次查询期间手动清空了缓存
- 同一个SqlSession两次查询期间提交了事务



[上一节](verse01.html) [回目录](index.html) [下一节](verse03.html)