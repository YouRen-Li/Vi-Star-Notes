[[toc]]

# 第五节 @RequestHeader注解

## 1、作用

通过这个注解获取请求消息头中的具体数据。



## 2、用法

```java
@RequestMapping("/request/header")
public String getRequestHeader(
    
        // 使用 @RequestHeader 注解获取请求消息头信息
        // name 或 value 属性：指定请求消息头名称
        // defaultValue 属性：设置默认值
        @RequestHeader(name = "Accept", defaultValue = "missing") String accept
) {
    
    logger.debug("accept = " +accept);
    
    return "target";
}
```



[上一节](verse04.html) [回目录](index.html) [下一节](verse06.html)