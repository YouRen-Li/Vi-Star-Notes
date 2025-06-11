[[toc]]

# 实验四 事务属性：超时

## 1、需求

事务在执行过程中，有可能因为遇到某些问题，导致程序卡住，从而长时间占用数据库资源。而长时间占用资源，大概率是因为程序运行出现了问题（可能是Java程序或MySQL数据库或网络连接等等）。

此时这个很可能出问题的程序应该被回滚，撤销它已做的操作，事务结束，把资源让出来，让其他正常程序可以执行。

概括来说就是一句话：超时回滚，释放资源。



## 2、设置

### ①@Transactional注解中的设置

```java
@Transactional(readOnly = false, timeout = 3)
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
```



### ②Dao方法中让线程睡眠

```java
public void updateEmpSalaryById(Integer empId, Double salary) {

    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // 为了看到操作失败后的效果人为将 SQL 语句破坏
    String sql = "update t_emp set emp_salary=? where emp_id=?";
    jdbcTemplate.update(sql, salary, empId);
}
```

> PS：注意：sleep操作如果放在执行 SQL 语句后面那就不起作用。



### ③执行效果

执行过程中日志和抛出异常的情况：

> [16:25:41.706] [DEBUG] [main] [org.springframework.jdbc.datasource.DataSourceTransactionManager] [Initiating transaction rollback]
> [16:25:41.706] [DEBUG] [main] [org.springframework.jdbc.datasource.DataSourceTransactionManager] [Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@53b7f657]]
> [16:25:41.709] [DEBUG] [main] [org.springframework.jdbc.datasource.DataSourceTransactionManager] [Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@53b7f657] after transaction]
>
> org.springframework.transaction.<span style="color:blue;font-weight:bold;">TransactionTimedOutException</span>: Transaction timed out: deadline was Fri Jun 04 16:25:39 CST 2021



[上一个实验](experiment03.html) [回目录](../verse03.html) [下一个实验](experiment05.html)