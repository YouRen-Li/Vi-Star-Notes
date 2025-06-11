[[toc]]

# 第八节 其他不重要内容

## 1、SpringMVC 配置文件的默认位置

### ①配置要求

- 配置文件存放目录：/WEB-INF 目录
- 文件名格式：[servlet-name]-servlet.xml
  - servlet-name 部分是在 web.xml 中配置 DispatcherServlet 时，servlet-name 标签的值
- 省略原理的 init-param



### ②为什么不建议

除 web.xml 是 Tomcat 要求放在 WEB-INF 下，其他配置文件习惯上是放在类路径下。



## 2、请求映射其他方式

### ①根据请求参数情况映射

使用 @RequestMapping 注解的 params 参数实现，表达式语法参见下面的例子：

| 需求                                                         | 映射方式                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 请求参数中必须包含userName                                   | @RequestMapping(value = "/xxx", <br />params="userName")     |
| 请求参数中不能包含userName                                   | @RequestMapping(value = "/xxx", <br />params="!userName")    |
| 请求参数中必须包含userName<br />且值必须为Tom2015            | @RequestMapping(value = "/xxx", <br />params="userName=Tom2015") |
| 请求参数中必须包含userName<br />但值不能为Tom2015            | @RequestMapping(value = "/xxx", <br />params="userName=!Tom2015") |
| 请求参数中必须包含userName<br />且值为Tom2015，<br />同时必须包含userPwd但值不限 | @RequestMapping(value = "/xxx", <br />params={"userName=Tom2015","userPwd"} ) |



### ②根据请求消息头内容映射

使用 @RequestMapping 注解的 headers 参数实现，表达式语法参见下面的例子：

| 需求                                     | 映射方式                                                     |
| ---------------------------------------- | ------------------------------------------------------------ |
| 根据 Accept-Language:zh-CN,zh;q=0.8 映射 | @RequestMapping (<br />value="/xxx",<br />headers= "Accept-Language=zh-CN,en;q=0.8" ) |



### ③Ant 风格通配符

- 英文问号：匹配一个字符
- 一个星号：匹配路径中的一层
- 两个连续星号：匹配路径中的多层



## 3、@ModelAttribute 注解

handler 类中，选定一个方法标记 @ModelAttribute 注解。

- 效果1：在每个 handler 方法前执行
- 效果2：可以将某些数据提前存入请求域

```java
@Controller
public class ModelAttrHandler {
 
    @ModelAttribute
    public void doSthBefore(Model model) {
        model.addAttribute("initAttr", "initValue");
    }
 
    @RequestMapping("/test/model/attr/one")
    public String testModelAttrOne(Model model) {
 
        Object modelAttribute = model.getAttribute("initAttr");
        System.out.println("modelAttribute = " + modelAttribute);
 
        return "target";
    }
 
    @RequestMapping("/test/model/attr/two")
    public String testModelAttrTwo(Model model) {
 
        Object modelAttribute = model.getAttribute("initAttr");
        System.out.println("modelAttribute = " + modelAttribute);
 
        return "target";
    }
 
    @RequestMapping("/test/model/attr/three")
    public String testModelAttrThree(Model model) {
 
        Object modelAttribute = model.getAttribute("initAttr");
        System.out.println("modelAttribute = " + modelAttribute);
 
        return "target";
    }
 
}
```



[上一节](verse07.html) [回目录](index.html)