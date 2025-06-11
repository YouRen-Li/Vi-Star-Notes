[[toc]]

# 实验六 事务属性：事务隔离级别

## 1、视角需要提升

![images](../images/img009.png)



## 2、测试的准备工作

### ①思路

![images](../images/img010.png)



### ②EmpService中参与测试的方法

```java
// readOnly = true把当前事务设置为只读
// @Transactional(readOnly = true)
public String getEmpName(Integer empId) {
    
    return empDao.selectEmpNameById(empId);
}
    
@Transactional(readOnly = false)
public void updateEmpName(Integer empId, String empName) {
    
    empDao.updateEmpNameById(empId, empName);
}
```



### ③junit中执行测试的方法

```java
@Test
public void testTxReadOnly() {
    
    String empName = empService.getEmpName(3);
    
    System.out.println("empName = " + empName);
    
}

@Test
public void testIsolation() {
    
    Integer empId = 2;
    String empName = "aaaaaaaa";
    
    empService.updateEmpName(empId, empName);
    
}
```



### ④搞破坏

为了让事务B（执行修改操作的事务）能够回滚，在EmpDao中的对应方法中人为抛出异常。

```java
public void updateEmpNameById(Integer empId, String empName) {
    String sql = "update t_emp set emp_name=? where emp_id=?";
    jdbcTemplate.update(sql, empName, empId);
    System.out.println(10 / 0);
}
```



## 3、执行测试

在 @Transactional 注解中使用 isolation 属性设置事务的隔离级别。 取值使用 org.springframework.transaction.annotation.Isolation 枚举类提供的数值。



### ①测试读未提交

```java
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public String getEmpName(Integer empId) {
    
    return empDao.selectEmpNameById(empId);
}
    
@Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = false)
public void updateEmpName(Integer empId, String empName) {
    
    empDao.updateEmpNameById(empId, empName);
}
```

![images](../images/img011.png)



测试结果：执行查询操作的事务读取了另一个尚未提交的修改。



### ②测试读已提交

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
public String getEmpName(Integer empId) {
    
    return empDao.selectEmpNameById(empId);
}
    
@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false)
public void updateEmpName(Integer empId, String empName) {
    
    empDao.updateEmpNameById(empId, empName);
}
```



测试结果：执行查询操作的事务读取的是数据库中正确的数据。



[上一个实验](experiment05.html) [回目录](../verse03.html) [下一个实验](experiment07.html)