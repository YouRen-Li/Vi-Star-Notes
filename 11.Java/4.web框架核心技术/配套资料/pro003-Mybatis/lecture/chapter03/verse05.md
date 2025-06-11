[[toc]]

# 第五节 延迟加载

## 1、概念

查询到Customer的时候，不一定会使用Order的List集合数据。如果Order的集合数据始终没有使用，那么这部分数据占用的内存就浪费了。对此，我们希望不一定会被用到的数据，能够在需要使用的时候再去查询。<br/>

例如：对Customer进行1000次查询中，其中只有15次会用到Order的集合数据，那么就在需要使用时才去查询能够大幅度节约内存空间。<br/>

<span style="color:blue;font-weight:bold;">延迟加载</span>的概念：对于实体类关联的属性到<span style="color:blue;font-weight:bold;">需要使用</span>时才查询。也叫<span style="color:blue;font-weight:bold;">懒加载</span>。



## 2、配置

![./images](./images/img020.png)



### ①较低版本

在Mybatis全局配置文件中配置settings

```xml
<!-- 使用settings对Mybatis全局进行设置 -->
<settings>
    <!-- 开启延迟加载功能：需要配置两个配置项 -->
    <!-- 1、将lazyLoadingEnabled设置为true，开启懒加载功能 -->
    <setting name="lazyLoadingEnabled" value="true"/>

    <!-- 2、将aggressiveLazyLoading设置为false，关闭“积极的懒加载” -->
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

> 官方文档中对aggressiveLazyLoading属性的解释：
>
> When enabled, an object with lazy loaded properties will be loaded entirely upon a call to any of the lazy properties.Otherwise, each property is loaded on demand.



### ②较高版本

```xml
<!-- Mybatis全局配置 -->
<settings>
    <!-- 开启延迟加载功能 -->
    <setting name="lazyLoadingEnabled" value="true"/>
</settings>
```



## 3、修改junit测试

```java
@Test
public void testSelectCustomerWithOrderList() throws InterruptedException {
    
    CustomerMapper mapper = session.getMapper(CustomerMapper.class);
    
    Customer customer = mapper.selectCustomerWithOrderList(1);
    
    // 这里必须只打印“customerId或customerName”这样已经加载的属性才能看到延迟加载的效果
    // 这里如果打印Customer对象整体则看不到效果
    System.out.println("customer = " + customer.getCustomerName());
    
    // 先指定具体的时间单位，然后再让线程睡一会儿
    TimeUnit.SECONDS.sleep(5);
    
    List<Order> orderList = customer.getOrderList();
    
    for (Order order : orderList) {
        System.out.println("order = " + order);
    }
}
```



效果：刚开始先查询Customer本身，需要用到OrderList的时候才发送SQL语句去查询

```java
DEBUG 11-30 11:25:31,127 ==>  Preparing: select customer_id,customer_name from t_customer where customer_id=?   (BaseJdbcLogger.java:145) 
DEBUG 11-30 11:25:31,193 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 11-30 11:25:31,314 <==      Total: 1  (BaseJdbcLogger.java:145) 
customer = c01
DEBUG 11-30 11:25:36,316 ==>  Preparing: select order_id,order_name from t_order where customer_id=?   (BaseJdbcLogger.java:145) 
DEBUG 11-30 11:25:36,316 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145) 
DEBUG 11-30 11:25:36,321 <==      Total: 3  (BaseJdbcLogger.java:145) 
order = Order{orderId=1, orderName='o1'}
order = Order{orderId=2, orderName='o2'}
order = Order{orderId=3, orderName='o3'}
```



## 4、关键词总结

我们是在“对多”关系中举例说明延迟加载的，在“对一”中配置方式基本一样。

| 关联关系     | 配置项关键词                                                 | 所在配置文件                    |
| ------------ | ------------------------------------------------------------ | ------------------------------- |
| 对一         | association标签/javaType属性                                 | Mapper配置文件中的resultMap     |
| 对多         | collection标签/ofType属性                                    | Mapper配置文件中的resultMap     |
| 对一分步     | association标签/select属性                                   | Mapper配置文件中的resultMap     |
| 对多分步     | collection标签/select属性                                    | Mapper配置文件中的resultMap     |
| 延迟加载[低] | lazyLoadingEnabled设置为true<br />aggressiveLazyLoading设置为false | Mybatis全局配置文件中的settings |
| 延迟加载[高] | lazyLoadingEnabled设置为true                                 | Mybatis全局配置文件中的settings |



[上一节](verse04.html) [回目录](index.html) [下一节](verse06.html)