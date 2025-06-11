[[toc]]

# 第二节 对一

## 1、创建OrderMapper接口

```java
public interface OrderMapper {
    
    Order selectOrderWithCustomer(Integer orderId);
    
}
```



## 2、创建OrderMapper.xml配置文件

```xml
<!-- 创建resultMap实现“对一”关联关系映射 -->
<!-- id属性：通常设置为这个resultMap所服务的那条SQL语句的id加上“ResultMap” -->
<!-- type属性：要设置为这个resultMap所服务的那条SQL语句最终要返回的类型 -->
<resultMap id="selectOrderWithCustomerResultMap" type="com.atguigu.mybatis.entity.Order">

    <!-- 先设置Order自身属性和字段的对应关系 -->
    <id column="order_id" property="orderId"/>
    <result column="order_name" property="orderName"/>

    <!-- 使用association标签配置“对一”关联关系 -->
    <!-- property属性：在Order类中对一的一端进行引用时使用的属性名 -->
    <!-- javaType属性：一的一端类的全类名 -->
    <association property="customer" javaType="com.atguigu.mybatis.entity.Customer">
        <!-- 配置Customer类的属性和字段名之间的对应关系 -->
        <id column="customer_id" property="customerId"/>
        <result column="customer_name" property="customerName"/>
    </association>

</resultMap>

<!-- Order selectOrderWithCustomer(Integer orderId); -->
<select id="selectOrderWithCustomer" resultMap="selectOrderWithCustomerResultMap">
    SELECT order_id,order_name,c.customer_id,customer_name
    FROM t_order o
    LEFT JOIN t_customer c
    ON o.customer_id=c.customer_id
    WHERE o.order_id=#{orderId}
</select>
```



对应关系可以参考下图：

![./images](./images/img018.png)



## 3、在Mybatis全局配置文件中注册Mapper配置文件

```xml
<!-- 注册Mapper配置文件：告诉Mybatis我们的Mapper配置文件的位置 -->
<mappers>
    <!-- 在mapper标签的resource属性中指定Mapper配置文件以“类路径根目录”为基准的相对路径 -->
    <mapper resource="com/atguigu/mybatis/mapper/OrderMapper.xml"/>
</mappers>
```



## 4、junit测试程序

```java
@Test
public void testRelationshipToOne() {
    OrderMapper orderMapper = session.getMapper(OrderMapper.class);
    
    // 查询Order对象，检查是否同时查询了关联的Customer对象
    Order order = orderMapper.selectOrderWithCustomer(2);
    System.out.println("order = " + order);
}
```



## 5、关键词

在“对一”关联关系中，我们的配置比较多，但是关键词就只有：<span style="color:blue;font-weight:bold;">association</span>和<span style="color:blue;font-weight:bold;">javaType</span>



[上一节](verse01.html) [回目录](index.html) [下一节](verse03.html)