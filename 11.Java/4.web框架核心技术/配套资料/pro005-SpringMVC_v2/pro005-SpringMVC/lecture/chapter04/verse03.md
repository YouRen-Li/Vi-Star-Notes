[[toc]]

# 第三节 类型转换

SpringMVC 将『把请求参数注入到 POJO 对象』这个操作称为<span style="color:blue;font-weight:bold;">『数据绑定』</span>，英文单词是 binding。数据类型的转换和格式化就发生在数据绑定的过程中。 类型转换和格式化是密不可分的两个过程，很多带格式的数据必须明确指定格式之后才可以进行类型转换。最典型的就是日期类型。



## 1、自动类型转换

HTTP 协议是一个无类型的协议，我们在服务器端接收到请求参数等形式的数据时，本质上都是字符串类型。请看 javax.servlet.ServletRequest 接口中获取全部请求参数的方法：

```java
public Map<String, String[]> getParameterMap();
```



而我们在实体类当中需要的类型是非常丰富的。对此，SpringMVC 对基本数据类型提供了自动的类型转换。例如：请求参数传入“100”字符串，我们实体类中需要的是 Integer 类型，那么 SpringMVC 会自动将字符串转换为 Integer 类型注入实体类。



## 2、日期和数值类型

### ①通过注解设定数据格式

```java
public class Product {
 
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productDate;
 
    @NumberFormat(pattern = "###,###,###.###")
    private Double productPrice;
```



### ②表单

```html
<form th:action="@{/save/product}" method="post">
    生产日期：<input type="text" name="productDate" value="1992-10-15 17:15:06" /><br/>
    产品价格：<input type="text" name="productPrice" value="111,222,333.444" /><br/>
    <button type="submit">保存</button>
</form>
```



### ③handler 方法

```java
@RequestMapping("/save/product")
public String saveProduct(Product product) {
 
    logger.debug(product.toString());
 
    return "target";
}
```



## 3、转换失败后处理方式

### ①默认结果

![iamges](./images/img010.png)



### ②BindingResult 接口

![iamges](./images/img011.png)

BindingResult 接口和它的父接口 Errors 中定义了很多和数据绑定相关的方法，如果在数据绑定过程中发生了错误，那么通过这个接口类型的对象就可以获取到相关错误信息。



### ③重构 handler 方法

```java
@RequestMapping("/save/product")
public String saveProduct(
        Product product,
 
	    // 在实体类参数和 BindingResult 之间不能有任何其他参数
        // 封装数据绑定结果的对象
        BindingResult bindingResult) {
 
    // 判断数据绑定过程中是否发生了错误
    if (bindingResult.hasErrors()) {
        // 如果发生了错误，则跳转到专门显示错误信息的页面
        // 相关错误信息会自动被放到请求域
        return "error";
    }
 
    logger.debug(product.toString());
 
    return "target";
}
```



### ④在页面上显示错误消息

```html
    <!-- th:errors 属性：用来显示请求处理过程中发生的错误 -->
    <!-- th:errors 属性值：访问错误信息的表达式 -->
    <!-- 访问错误信息的表达式：访问请求域，需要使用 ${} 格式 -->
    <!-- 访问请求域使用的属性名：执行数据绑定的实体类的简单类名首字母小写 -->
    <!-- 具体错误信息：找到实体类之后进一步访问出问题的属性名 -->
    <p th:errors="${product.productDate}">这里显示具体错误信息</p>
```



## 4、自定义类型转换器

在实际开发过程中，难免会有某些情况需要使用自定义类型转换器。因为我们自己自定义的类型在 SpringMVC 中没有对应的内置类型转换器。此时需要我们提供自定义类型来执行转换。

::: tip

我们学习的知识点可以分成：

- 拼死学会

- 以防万一

- 增长见闻

  自定义类型转换器的定位就是以防万一

:::



### ①创建实体类

#### [1]Address

```java
public class Address {
 
    private String province;
    private String city;
    private String street;
    ……
```



#### [2]Student

```java
public class Student {
 
    private Address address;
    ……
```



### ②表单

现在我们希望通过一个文本框输入约定格式的字符串，然后转换为我们需要的类型，所以必须通过自定义类型转换器来实现，否则 SpringMVC 无法识别。

```html
<h3>自定义类型转换器</h3>
<form th:action="@{/save/student}" method="post">
    地址：<input type="text" name="address" value="aaa,bbb,ccc" /><br/>
</form>
```



### ③handler 方法

```java
@RequestMapping("/save/student")
public String saveStudent(Student student) {
 
    logger.debug(student.getAddress().toString());
 
    return "target";
}
```



在目前代码的基础上，我们没有提供自定义类型转换器，所以处理请求时看到如下错误日志：

> Field error in object 'student' on field 'address': rejected value [aaa,bbb,ccc]; codes [typeMismatch.student.address,typeMismatch.address,typeMismatch.com.atguigu.mvc.entity.Address,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [student.address,address]; arguments []; default message [address]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'com.atguigu.mvc.entity.Address' for property 'address'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'com.atguigu.mvc.entity.Address' for property 'address': no matching editors or conversion strategy found]]]

页面返回 400。



### ④创建自定义类型转换器类

实现接口：org.springframework.core.convert.converter.Converter&lt;S,T&gt;

泛型 S：源类型（本例中是 String 类型）

泛型 T：目标类型（本例中是 Address 类型）

```java
public class AddressConverter implements Converter<String, Address> {
    @Override
    public Address convert(String source) {
  
        // 1.按照约定的规则拆分源字符串
        String[] split = source.split(",");
         
        String province = split[0];
        String city = split[1];
        String street = split[2];
 
        // 2.根据拆分结果创建 Address 对象
        Address address = new Address(province, city, street);
         
        // 3.返回转换得到的对象
        return address;
    }
}
```



### ⑤在 SpringMVC 中注册

```xml
<!-- 在 mvc:annotation-driven 中注册 FormattingConversionServiceFactoryBean -->
<mvc:annotation-driven conversion-service="formattingConversionService"/>
 
<!-- 在 FormattingConversionServiceFactoryBean 中注册自定义类型转换器 -->
<bean id="formattingConversionService"
      class="org.springframework.format.support.FormattingConversionServiceFactoryBean">

    <!-- 在 converters 属性中指定自定义类型转换器 -->
    <property name="converters">
        <set>
            <bean class="com.atguigu.mvc.converter.AddressConverter"/>
        </set>
    </property>
 
</bean>
```



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)