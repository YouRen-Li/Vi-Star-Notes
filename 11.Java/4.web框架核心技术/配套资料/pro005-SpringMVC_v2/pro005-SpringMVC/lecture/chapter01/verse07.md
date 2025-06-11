[[toc]]

# 第七节 页面跳转控制

## 1、准备工作

- 准备一个地址在前后缀范围之外的页面
- 让这个页面能够成功访问



### ①创建范围之外的页面

![./images](./images/img009.png)

```html
<body>
    
    <h1>范围之外页面</h1>
    
</body>
```



### ②在 SpringMVC 配置文件加入配置

下面配置是访问静态资源所需配置，后面会专门说，现在先直接拿来用：

```xml
    <mvc:annotation-driven/>
    <mvc:default-servlet-handler/>
```



## 2、使用指令

### ①转发指令

```java
@RequestMapping("/test/forward/command")
public String forwardCommand() {
    
    // 需求：要转发前往的目标地址不在视图前缀指定的范围内，
    // 通过返回逻辑视图、拼接前缀后缀得到的物理视图无法达到目标地址
    
    // 转发到指定的地址：
    return "forward:/outter.html";
}
```



### ②重定向指令

```java
@RequestMapping("/test/redirect/command")
public String redirectCommand() {
    
    // 重定向到指定的地址：
    // 这个地址由 SpringMVC 框架负责在前面附加 contextPath，所以我们不能加，我们加了就加多了
    // 框架增加 contextPath 后：/demo/outter.html
    // 我们多加一个：/demo/demo/outter.html
    return "redirect:/outter.html";
}
```



[上一节](verse06.html) [回目录](index.html)