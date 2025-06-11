[[toc]]

# 实验三 事务属性：只读

## 1、介绍

对一个查询操作来说，如果我们把它设置成只读，就能够明确告诉数据库，这个操作不涉及写操作。这样数据库就能够针对查询操作来进行优化。



## 2、设置方式

```java
// readOnly = true把当前事务设置为只读
@Transactional(readOnly = true)
public String getEmpName(Integer empId) {
      
    return empDao.selectEmpNameById(empId);
}
```



## 3、针对增删改操作设置只读

会抛出下面异常：

> Caused by: java.sql.SQLException: Connection is read-only. Queries leading to data modification are not allowed



## 4、@Transactional注解放在类上

### ①生效原则

如果一个类中每一个方法上都使用了@Transactional注解，那么就可以将@Transactional注解提取到类上。反过来说：@Transactional注解在类级别标记，会影响到类中的每一个方法。同时，类级别标记的@Transactional注解中设置的事务属性也会延续影响到方法执行时的事务属性。除非在方法上又设置了@Transactional注解。

对一个方法来说，离它最近的@Transactional注解中的事务属性设置生效。



### ②用法举例

在类级别@Transactional注解中设置只读，这样类中所有的查询方法都不需要设置@Transactional注解了。因为对查询操作来说，其他属性通常不需要设置，所以使用公共设置即可。

然后在这个基础上，对增删改方法设置@Transactional注解 readOnly 属性为 false。

```java
@Service
@Transactional(readOnly = true)
public class EmpService {
    
    // 为了便于核对数据库操作结果，不要修改同一条记录
    @Transactional(readOnly = false)
    public void updateTwice(……) {
		……
    }
    
    // readOnly = true把当前事务设置为只读
    // @Transactional(readOnly = true)
    public String getEmpName(Integer empId) {
		……
    }
    
}
```



> PS：Spring 环境下很多场合都有类似设定，一个注解如果标记了类的每一个方法那么通常就可以提取到类级别。



[上一个实验](experiment02.html) [回目录](../verse03.html) [下一个实验](experiment04.html)