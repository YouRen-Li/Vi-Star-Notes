[[toc]]

# 第一节 概述



## 1、SpringMVC 优势

SpringMVC 是 Spring 为表述层开发提供的一整套完备的解决方案。在表述层框架历经 Strust、WebWork、Strust2 等诸多产品的历代更迭之后，目前业界普遍选择了 SpringMVC 作为 Java EE 项目表述层开发的<span style="color:blue;font-weight:bold;">首选方案</span>。之所以能做到这一点，是因为 SpringMVC 具备如下显著优势：

- <span style="color:blue;font-weight:bold;">Spring 家族原生产品</span>，与 IOC 容器等基础设施无缝对接
- 表述层各细分领域需要解决的问题<span style="color:blue;font-weight:bold;">全方位覆盖</span>，提供<span style="color:blue;font-weight:bold;">全面解决方案</span>
- <span style="color:blue;font-weight:bold;">代码清新简洁</span>，大幅度提升开发效率
- 内部组件化程度高，可插拔式组件<span style="color:blue;font-weight:bold;">即插即用</span>，想要什么功能配置相应组件即可
- <span style="color:blue;font-weight:bold;">性能卓著</span>，尤其适合现代大型、超大型互联网项目要求



## 2、表述层框架要解决的基本问题

- 请求映射
- 数据输入
- 视图界面
- 请求分发
- 表单回显
- 会话控制
- 过滤拦截
- 异步交互
- 文件上传
- 文件下载
- 数据校验
- 类型转换



## 3、SpringMVC 代码对比

### ①基于原生 Servlet API 开发代码片段

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
    
    String userName = request.getParameter("userName");
    
    System.out.println("userName="+userName);
    
}
```



### ②基于 SpringMVC 开发代码片段

```java
@RequestMapping("/user/login")
public String login(@RequestParam("userName") String userName){
    
    System.out.println("userName="+userName);
    
    return "result";
}
```



[回目录](index.html) [下一节](verse02.html)