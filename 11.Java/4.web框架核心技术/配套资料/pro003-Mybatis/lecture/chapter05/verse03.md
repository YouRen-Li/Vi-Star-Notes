[[toc]]

# 第三节 二级缓存

这里我们使用的是Mybatis自带的二级缓存。



## 1、代码测试二级缓存

### ①开启二级缓存功能

在想要使用二级缓存的Mapper配置文件中加入cache标签

```xml
<mapper namespace="com.atguigu.mybatis.EmployeeMapper">
    
    <!-- 加入cache标签启用二级缓存功能 -->
    <cache/>
```



### ②让实体类支持序列化

```java
public class Employee implements Serializable {
```



### ③junit测试

这个功能的测试操作需要将SqlSessionFactory对象设置为成员变量

```java
@Test
public void testSecondLevelCacheExists() {
    SqlSession session = factory.openSession();
    
    EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
    
    Employee employee = mapper.selectEmployeeById(2);
    
    System.out.println("employee = " + employee);
    
    // 在执行第二次查询前，关闭当前SqlSession
    session.close();
    
    // 开启一个新的SqlSession
    session = factory.openSession();
    
    mapper = session.getMapper(EmployeeMapper.class);
    
    employee = mapper.selectEmployeeById(2);
    
    System.out.println("employee = " + employee);
    
    session.close();
    
}
```



打印效果：

```java
DEBUG 12-01 09:44:27,057 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
DEBUG 12-01 09:44:27,459 ==>  Preparing: select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp where emp_id=?   (BaseJdbcLogger.java:145) 
DEBUG 12-01 09:44:27,510 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 12-01 09:44:27,536 <==      Total: 1  (BaseJdbcLogger.java:145) 
employee = Employee{empId=2, empName='AAAAAA', empSalary=6666.66, empAge=20, empGender='male'}
DEBUG 12-01 09:44:27,622 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.5  (LoggingCache.java:62) 
employee = Employee{empId=2, empName='AAAAAA', empSalary=6666.66, empAge=20, empGender='male'}
```



### ④缓存命中率

日志中打印的Cache Hit Ratio叫做缓存命中率

```java
    Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.0（0/1)
    Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.5（1/2）
    Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.6666666666666666（2/3）
    Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.75（3/4）
    Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.8（4/5）
```

缓存命中率=命中缓存的次数/查询的总次数



## 2、查询结果存入二级缓存的时机

结论：SqlSession关闭的时候，一级缓存中的内容会被存入二级缓存

```java
// 1.开启两个SqlSession
SqlSession session01 = factory.openSession();
SqlSession session02 = factory.openSession();
    
// 2.获取两个EmployeeMapper
EmployeeMapper employeeMapper01 = session01.getMapper(EmployeeMapper.class);
EmployeeMapper employeeMapper02 = session02.getMapper(EmployeeMapper.class);
    
// 3.使用两个EmployeeMapper做两次查询，返回两个Employee对象
Employee employee01 = employeeMapper01.selectEmployeeById(2);
Employee employee02 = employeeMapper02.selectEmployeeById(2);
    
// 4.比较两个Employee对象
System.out.println("employee02.equals(employee01) = " + employee02.equals(employee01));
```



上面代码打印的结果是：

```java
DEBUG 12-01 10:10:32,209 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
DEBUG 12-01 10:10:32,570 ==>  Preparing: select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp where emp_id=?   (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:10:32,624 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:10:32,643 <==      Total: 1  (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:10:32,644 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
DEBUG 12-01 10:10:32,661 ==>  Preparing: select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp where emp_id=?   (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:10:32,662 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:10:32,665 <==      Total: 1  (BaseJdbcLogger.java:145) 
employee02.equals(employee01) = false
```



修改代码：

```java
// 1.开启两个SqlSession
SqlSession session01 = factory.openSession();
SqlSession session02 = factory.openSession();
    
// 2.获取两个EmployeeMapper
EmployeeMapper employeeMapper01 = session01.getMapper(EmployeeMapper.class);
EmployeeMapper employeeMapper02 = session02.getMapper(EmployeeMapper.class);
    
// 3.使用两个EmployeeMapper做两次查询，返回两个Employee对象
Employee employee01 = employeeMapper01.selectEmployeeById(2);
    
// ※第一次查询完成后，把所在的SqlSession关闭，使一级缓存中的数据存入二级缓存
session01.close();
Employee employee02 = employeeMapper02.selectEmployeeById(2);
    
// 4.比较两个Employee对象
System.out.println("employee02.equals(employee01) = " + employee02.equals(employee01));
    
// 5.另外一个SqlSession用完正常关闭
session02.close();
```



打印结果：

```java
DEBUG 12-01 10:14:06,804 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
DEBUG 12-01 10:14:07,135 ==>  Preparing: select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp where emp_id=?   (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:14:07,202 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:14:07,224 <==      Total: 1  (BaseJdbcLogger.java:145) 
DEBUG 12-01 10:14:07,308 Cache Hit Ratio [com.atguigu.mybatis.EmployeeMapper]: 0.5  (LoggingCache.java:62) 
employee02.equals(employee01) = false
```



## 3、二级缓存相关配置

在Mapper配置文件中添加的cache标签可以设置一些属性：

- eviction属性：缓存回收策略

  LRU（Least Recently Used） – 最近最少使用的：移除最长时间不被使用的对象。

  FIFO（First in First out） – 先进先出：按对象进入缓存的顺序来移除它们。

  SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。

  WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。

  默认的是 LRU。

- flushInterval属性：刷新间隔，单位毫秒

  默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新

- size属性：引用数目，正整数

  代表缓存最多可以存储多少个对象，太大容易导致内存溢出

- readOnly属性：只读，true/false

  true：只读缓存；会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。

  false：读写缓存；会返回缓存对象的拷贝（通过序列化）。这会慢一些，但是安全，因此默认是 false。



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)