[[toc]]

# 实验五 事务属性：回滚和不回滚的异常

## 1、默认情况

默认只针对运行时异常回滚，编译时异常不回滚。情景模拟代码如下：

```java
public void updateEmpSalaryById(Integer empId, Double salary) throws FileNotFoundException {
    
	// 为了看到操作失败后的效果人为将 SQL 语句破坏
	String sql = "update t_emp set emp_salary=? where emp_id=?";
	jdbcTemplate.update(sql, salary, empId);
    
//  抛出编译时异常测试是否回滚
	new FileInputStream("aaaa.aaa");
    
//  抛出运行时异常测试是否回滚
//  System.out.println(10 / 0);
}
```



## 2、设置回滚的异常

- rollbackFor属性：需要设置一个Class类型的对象
- rollbackForClassName属性：需要设置一个字符串类型的全类名

```java
@Transactional(rollbackFor = Exception.class)
```



## 3、设置不回滚的异常

在默认设置和已有设置的基础上，再指定一个异常类型，碰到它不回滚。

```java
    @Transactional(
            noRollbackFor = FileNotFoundException.class
    )
```



## 4、回滚和不回滚异常同时设置

### ①范围不同

不管是哪个设置范围大，都是在大范围内在排除小范围的设定。例如：

- rollbackFor = Exception.class
- noRollbackFor = FileNotFoundException.class

意思是除了 FileNotFoundException 之外，其他所有 Exception 范围的异常都回滚；但是碰到 FileNotFoundException 不回滚。



### ②范围一致

回滚和不回滚的异常设置了相同范围（这是有多想不开）：

- noRollbackFor = FileNotFoundException.class
- rollbackFor = FileNotFoundException.class

此时 Spring 采纳了 rollbackFor 属性的设定：遇到 FileNotFoundException 异常会回滚。



[上一个实验](experiment04.html) [回目录](../verse03.html) [下一个实验](experiment06.html)