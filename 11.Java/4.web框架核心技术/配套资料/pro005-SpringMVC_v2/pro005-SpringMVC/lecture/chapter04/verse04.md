[[toc]]

# 第四节 数据校验

在 Web 应用三层架构体系中，表述层负责接收浏览器提交的数据，业务逻辑层负责数据的处理。为了能够让业务逻辑层基于正确的数据进行处理，我们需要在表述层对数据进行检查，将错误的数据隔绝在业务逻辑层之外。



## 1、校验概述

JSR 303 是 Java 为 Bean 数据 hé fǎ 性校验提供的标准框架，它已经包含在 JavaEE 6.0 标准中。JSR 303 通过在 Bean 属性上标注类似于 @NotNull、@Max 等标准的注解指定校验规则，并通过标准的验证接口对Bean进行验证。



| 注解                       | 规则                                           |
| -------------------------- | ---------------------------------------------- |
| @Null                      | 标注值必须为 null                              |
| @NotNull                   | 标注值不可为 null                              |
| @AssertTrue                | 标注值必须为 true                              |
| @AssertFalse               | 标注值必须为 false                             |
| @Min(value)                | 标注值必须大于或等于 value                     |
| @Max(value)                | 标注值必须小于或等于 value                     |
| @DecimalMin(value)         | 标注值必须大于或等于 value                     |
| @DecimalMax(value)         | 标注值必须小于或等于 value                     |
| @Size(max,min)             | 标注值大小必须在 max 和 min 限定的范围内       |
| @Digits(integer,fratction) | 标注值值必须是一个数字，且必须在可接受的范围内 |
| @Past                      | 标注值只能用于日期型，且必须是过去的日期       |
| @Future                    | 标注值只能用于日期型，且必须是将来的日期       |
| @Pattern(value)            | 标注值必须符合指定的正则表达式                 |



JSR 303 只是一套标准，需要提供其实现才可以使用。Hibernate Validator 是 JSR 303 的一个参考实现，除支持所有标准的校验注解外，它还支持以下的扩展注解：

| 注解      | 规则                               |
| --------- | ---------------------------------- |
| @Email    | 标注值必须是格式正确的 Email 地址  |
| @Length   | 标注值字符串大小必须在指定的范围内 |
| @NotEmpty | 标注值字符串不能是空字符串         |
| @Range    | 标注值必须在指定的范围内           |



Spring 4.0 版本已经拥有自己独立的数据校验框架，同时支持 JSR 303 标准的校验框架。Spring 在进行数据绑定时，可同时调用校验框架完成数据校验工作。在SpringMVC 中，可直接通过注解驱动 mvc:annotation-driven 的方式进行数据校验。Spring 的 LocalValidatorFactoryBean 既实现了 Spring 的 Validator 接口，也实现了 JSR 303 的 Validator 接口。只要在Spring容器中定义了一个LocalValidatorFactoryBean，即可将其注入到需要数据校验的 Bean中。Spring本身并没有提供JSR 303的实现，所以必须将JSR 303的实现者的jar包放到类路径下。



配置 mvc:annotation-driven 后，SpringMVC 会默认装配好一个 LocalValidatorFactoryBean，通过<span style="color:blue;font-weight:bold;">在处理方法的入参上标注 @Validated 注解</span>即可让 SpringMVC 在完成数据绑定后执行数据校验的工作。



## 2、操作演示

请在 SpringMVC 环境基础上做下面的操作：



### ①导入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.2.0.Final</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator-annotation-processor -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator-annotation-processor</artifactId>
    <version>6.2.0.Final</version>
</dependency>
```

注：需要 Tomcat 版本至少是 8。并不是说 Tomcat 6 不能做 SpringMVC 的数据校验，而是所需依赖不同。



### ②应用校验规则

#### [1]标记规则注解

在实体类中需要附加校验规则的成员变量上标记校验规则注解：

```java
    // 字符串长度：[3,6]
    @Size(min = 3, max = 6)
    
    // 字符串必须满足Email格式
    @Email
    private String email;
```



#### [2]在handler 方法形参标记注解

```java
@RequestMapping("/save/person")
public String saveperson(@Validated person person) {
 
    logger.debug(person.getEmail());
 
    return "target";
}
```



#### [3]校验失败效果

日志：

> Field error in object 'person' on field 'email': rejected value [aa]; codes [Email.person.email,Email.email,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [person.email,email]; arguments []; default message [email],[Ljavax.validation.constraints.Pattern$Flag;@4a6addb7,.*]; default message [不是一个 hé fǎ 的电子邮件地址]
> Field error in object 'person' on field 'email': rejected value [aa]; codes [Size.person.email,Size.email,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [person.email,email]; arguments []; default message [email],6,3]; default message [个数必须在3和6之间]]]

同时页面返回 400。



### ③显示友好的错误提示

#### [1]重构 handler 方法

```java
@RequestMapping("/save/person")
public String savePerson(
         
        // 在实体类参数和 BindingResult 之间不能有任何其他参数
        @Validated Person person, BindingResult bindingResult) {
 
    if (bindingResult.hasErrors()) {
        return "error";
    }
     
    logger.debug(person.getEmail());
 
    return "target";
}
```



#### [2]准备错误信息页面

```html
    <h1>系统信息</h1>
    <!-- 从请求域获取实体类信息时，属性名是按照类名首字母小写的规则 -->
    <!-- ${注入请求参数的实体类.出问题的字段} -->
    <p th:errors="${person.email}">这里显示系统提示消息</p>
```



[上一节](verse03.html) [回目录](index.html) [下一节](verse05.html)