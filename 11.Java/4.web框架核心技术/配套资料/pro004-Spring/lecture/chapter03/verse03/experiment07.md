[[toc]]

# 实验七 事务属性：事务传播行为

## 1、事务传播行为要研究的问题

![images](../images/img012.png)



## 2、propagation属性

### ①默认值

@Transactional 注解通过 propagation 属性设置事务的传播行为。它的默认值是：

```java
Propagation propagation() default Propagation.REQUIRED;
```



### ②可选值说明

propagation 属性的可选值由 org.springframework.transaction.annotation.Propagation 枚举类提供：

| 名称                                                         | 含义                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| REQUIRED<br />默认值                                         | 当前方法必须工作在事务中<br />如果当前线程上有已经开启的事务可用，那么就在这个事务中运行<br />如果当千纤草上没有已经开启的事务，那么就自己开启新事务，在新事务中运行<br />所以当前方法有可能和其他方法共用事务<br />在共用事务的情况下：当前方法会因为其他方法回滚而受连累 |
| <span style="color:blue;font-weight:bold;">REQUIRES_NEW</span><br />建议使用 | 当前方法必须工作在事务中<br />不管当前线程上是否有已经开启的事务，都要开启新事务<br />在新事务中运行<br />不会和其他方法共用事务，避免被其他方法连累 |



## 3、测试

### ①创建测试方法

#### [1]在EmpService中声明两个内层方法

```java
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public void updateEmpNameInner(Integer empId, String empName) {
    
    empDao.updateEmpNameById(empId, empName);
}
    
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public void updateEmpSalaryInner(Integer empId, Double empSalary) {
    
    empDao.updateEmpSalaryById(empId, empSalary);
}
```



#### [2]创建TopService

![images](../images/img013.png)

```java
@Service
public class TopService {
    
    // 这里我们只是为了测试事务传播行为，临时在Service中装配另一个Service
    // 实际开发时非常不建议这么做，因为这样会严重破坏项目的结构
    @Autowired
    private EmpService empService;
    
    @Transactional
    public void topTxMethod() {
    
        // 在外层方法中调用两个内层方法
        empService.updateEmpNameInner(2, "aaa");
        
        empService.updateEmpSalaryInner(3, 666.66);
        
    }
    
}
```



#### [3]junit测试方法

```java
@Autowired
private TopService topService;
    
@Test
public void testPropagation() {
    
    // 调用外层方法
    topService.topTxMethod();
    
}
```



### ②测试 REQUIRED 模式

![images](../images/img014.png)

效果：内层方法A、内层方法B所做的修改都没有生效，总事务回滚了。



### ③测试 REQUIRES_NEW 模式

#### [1]修改 EmpService 中内层方法

```java
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public void updateEmpNameInner(Integer empId, String empName) {
    
    empDao.updateEmpNameById(empId, empName);
}
    
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public void updateEmpSalaryInner(Integer empId, Double empSalary) {
    
    empDao.updateEmpSalaryById(empId, empSalary);
}
```



#### [2]执行流程

![images](../images/img015.png)



## 4、实际开发情景

### ①Service方法应用了通知

![images](../images/img016.png)



### ②过滤器或拦截器等类似组件

![images](../images/img017.png)



### ③升华

我们在事务传播行为这里，使用 REQUIRES_NEW 属性，也可以说是让不同事务方法从事务的使用上<span style="color:blue;font-weight:bold;">解耦合</span>，不要互相影响。



[上一个实验](experiment06.html) [回目录](../verse03.html)