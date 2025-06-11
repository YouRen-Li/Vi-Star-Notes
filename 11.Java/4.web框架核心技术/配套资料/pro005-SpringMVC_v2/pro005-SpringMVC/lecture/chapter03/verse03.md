[[toc]]

# 第三节 @PathVariable注解

## 1、REST 风格路径参数

请看下面链接：

> /emp/20
>
> /shop/product/iphone

如果我们想要获取链接地址中的某个部分的值，就可以使用 @PathVariable 注解，例如上面地址中的20、iphone部分。



## 2、操作

### ①传一个值

#### [1]超链接

```html
<a th:href="@{/emp/20}">传一个值</a><br/>
```



#### [2]handler 方法

```java
// 实际访问地址：/emp/20
// 映射地址：/emp/{empId}是把变量部分用大括号标记出来，写入变量名
@RequestMapping("/emp/{empId}")
public String getEmpById(@PathVariable("empId") Integer empId) {
    
    logger.debug("empId = " + empId);
    
    return "target";
}
```



### ②传多个值

#### [1]超链接

```html
<a th:href="@{/emp/tom/18/50}">传多个值</a><br/>
```



#### [2]handler 方法

```java
// 实际地址：/emp/tom/18/50
@RequestMapping("/emp/{empName}/{empAge}/{empSalary}")
public String queryEmp(
        @PathVariable("empName") String empName,
        @PathVariable("empAge") Integer empAge,
        @PathVariable("empSalary") Double empSalary
) {
    
    logger.debug("empName = " + empName);
    logger.debug("empAge = " + empAge);
    logger.debug("empSalary = " + empSalary);
    
    return "target";
}
```



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)