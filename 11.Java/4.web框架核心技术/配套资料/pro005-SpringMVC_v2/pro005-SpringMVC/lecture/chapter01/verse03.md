[[toc]]

# 第三节 @RequestMapping注解

从注解名称上我们可以看到，@RequestMapping注解的作用就是将请求的 URL 地址和处理请求的方式（handler方法）关联起来，建立映射关系。

SpringMVC 接收到指定的请求，就会来找到在映射关系中对应的方法来处理这个请求。



## 1、匹配方式说明

### ①精确匹配

在@RequestMapping注解指定 URL 地址时，不使用任何通配符，按照请求地址进行精确匹配。

```html
<a th:href="@{/say/hello/to/spring/mvc}">HelloWorld</a><br/>
```



```java
@RequestMapping("/say/hello/to/spring/mvc")
```



### ②模糊匹配

在@RequestMapping注解指定 URL 地址时，通过使用通配符，匹配多个类似的地址。

```html
<h3>测试@RequestMapping注解匹配方式</h3>
<a th:href="@{/fruit/apple}">@RequestMapping模糊匹配[apple]</a><br/>
<a th:href="@{/fruit/orange}">@RequestMapping模糊匹配[orange]</a><br/>
<a th:href="@{/fruit/banana}">@RequestMapping模糊匹配[banana]</a><br/>
```



```java
@RequestMapping("/fruit/*")
```



## 2、在类级别标记

### ①超链接的HTML标签

```html
<h3>测试@RequestMapping注解标记在类上</h3>
<a th:href="@{/user/login}">用户登录</a><br/>
<a th:href="@{/user/register}">用户注册</a><br/>
<a th:href="@{/user/logout}">用户退出</a><br/>
```



### ②仅标记在方法上的@RequestMapping注解

```java
@RequestMapping("/user/login")
@RequestMapping("/user/register")
@RequestMapping("/user/logout")
```



### ③分别标记在类和方法上的@RequestMapping注解

在类级别：抽取各个方法上@RequestMapping注解地址中前面重复的部分

```java
@RequestMapping("/user")
```



在方法级别：省略被类级别抽取的部分

```java
@RequestMapping("/login")
@RequestMapping("/register")
@RequestMapping("/logout")
```



## 3、附加请求方式要求

### ①请求方式

HTTP 协议定义了八种请求方式，在 SpringMVC 中封装到了下面这个枚举类：

```java
public enum RequestMethod {

	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE

}
```



### ②@RequestMapping附加请求方式

前面代码中，只要求请求地址匹配即可，现在附加了请求方式后，还要求请求方式也必须匹配才可以。

#### [1]HTML代码

```html
<h3>测试@RequestMapping注解限定请求方式</h3>
<a th:href="@{/emp}">同地址GET请求</a><br/>
<form th:action="@{/emp}" method="post">
    <button type="submit">同地址POST请求</button>
</form>
<br/>
```



#### [2]handler方法

处理 GET 请求：

```java
@RequestMapping(value = "/emp", method = RequestMethod.GET)
public String empGet() {
    
    logger.debug("GET 请求");
    
    return "target";
}
```



处理 POST 请求：

```java
@RequestMapping(value = "/emp", method = RequestMethod.POST)
public String empPost() {
    
    logger.debug("POST 请求");
    
    return "target";
}
```



### ③进阶版

| 原版                                                         | 进阶版               |
| ------------------------------------------------------------ | -------------------- |
| @RequestMapping(value = "/emp", <br />method = RequestMethod.GET) | @GetMapping("/emp")  |
| @RequestMapping(value = "/emp", <br />method = RequestMethod.POST) | @PostMapping("/emp") |



除了 @GetMapping、@PostMapping 还有下面几个类似的注解：

- @see PutMapping
- @see DeleteMapping
- @see PatchMapping



另外需要注意：进阶版的这几个注解是从 4.3 版本才开始有，低于 4.3 版本无法使用。



## 4、Ambiguous mapping异常

出现原因：多个 handler 方法映射了同一个地址，导致 SpringMVC 在接收到这个地址的请求时该找哪个 handler 方法处理。

> Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'demo03MappingMethodHandler' method 
> com.atguigu.mvc.handler.Demo03MappingMethodHandler#empPost()
> to { [/emp]}: <span style="color:blue;font-weight:bold;">There is already</span> 'demo03MappingMethodHandler' bean method
> com.atguigu.mvc.handler.Demo03MappingMethodHandler#empGet() <span style="color:blue;font-weight:bold;">mapped</span>.



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)

