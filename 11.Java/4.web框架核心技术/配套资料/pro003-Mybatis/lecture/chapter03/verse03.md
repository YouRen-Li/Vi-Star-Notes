[[toc]]

# 第三节 对多

## 1、创建Mapper接口

```java
public interface CustomerMapper {
    
    Customer selectCustomerWithOrderList(Integer customerId);
    
}
```



## 2、创建CustomerMapper.xml配置文件

注意：不要忘记在Mybatis全局配置文件中注册



## 3、配置关联关系和SQL语句

```xml
<!-- 配置resultMap实现从Customer到OrderList的“对多”关联关系 -->
<resultMap id="selectCustomerWithOrderListResultMap"
           type="com.atguigu.mybatis.entity.Customer">
    
    <!-- 映射Customer本身的属性 -->
    <id column="customer_id" property="customerId"/>
    <result column="customer_name" property="customerName"/>
    
    <!-- collection标签：映射“对多”的关联关系 -->
    <!-- property属性：在Customer类中，关联“多”的一端的属性名 -->
    <!-- ofType属性：集合属性中元素的类型 -->
    <collection property="orderList" ofType="com.atguigu.mybatis.entity.Order">
        <!-- 映射Order的属性 -->
        <id column="order_id" property="orderId"/>
        <result column="order_name" property="orderName"/>
    
    </collection>
    
</resultMap>
    
<!-- Customer selectCustomerWithOrderList(Integer customerId); -->
<select id="selectCustomerWithOrderList" resultMap="selectCustomerWithOrderListResultMap">
    SELECT c.customer_id,c.customer_name,o.order_id,o.order_name
    FROM t_customer c
    LEFT JOIN t_order o
    ON c.customer_id=o.customer_id
    WHERE c.customer_id=#{customerId}
</select>
```



对应关系可以参考下图：

![./images](./images/img019.png)



## 4、junit测试

```java
@Test
public void testRelationshipToMulti() {
    
    CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
    
    // 查询Customer对象同时将关联的Order集合查询出来
    Customer customer = customerMapper.selectCustomerWithOrderList(1);
    
    System.out.println("customer.getCustomerId() = " + customer.getCustomerId());
    System.out.println("customer.getCustomerName() = " + customer.getCustomerName());
    
    List<Order> orderList = customer.getOrderList();
    for (Order order : orderList) {
        System.out.println("order = " + order);
    }
    
}
```



## 5、关键词

在“对多”关联关系中，同样有很多配置，但是提炼出来最关键的就是：“<span style="color:blue;font-weight:bold;">collection</span>”和“<span style="color:blue;font-weight:bold;">ofType</span>”



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)