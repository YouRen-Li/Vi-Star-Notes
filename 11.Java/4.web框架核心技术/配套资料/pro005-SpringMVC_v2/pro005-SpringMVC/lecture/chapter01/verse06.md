[[toc]]

# 第六节 @CookieValue注解

## 1、作用

获取当前请求中的 Cookie 数据。



## 2、用法

```java
@RequestMapping("/request/cookie")
public String getCookie(
    
        // 使用 @CookieValue 注解获取指定名称的 Cookie 数据
        // name 或 value 属性：指定Cookie 名称
        // defaultValue 属性：设置默认值
        @CookieValue(value = "JSESSIONID", defaultValue = "missing") String cookieValue,
    
        // 形参位置声明 HttpSession 类型的参数即可获取 HttpSession 对象
        HttpSession session
) {
    
    logger.debug("cookieValue = " + cookieValue);
    
    return "target";
}
```



[上一节](verse05.html) [回目录](index.html) [下一节](verse07.html)