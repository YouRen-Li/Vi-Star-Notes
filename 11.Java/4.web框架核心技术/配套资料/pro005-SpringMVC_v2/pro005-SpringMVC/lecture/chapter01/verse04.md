[[toc]]

# 第四节 获取请求参数

## 1、一名一值

### ①超链接

```html
<a th:href="@{/param/one/name/one/value(userName='tom')}">一个名字一个值的情况</a><br/>
```



### ②@RequestParam注解

#### [1]最基本的用法

```java
@RequestMapping("/param/one/name/one/value")
public String oneNameOneValue(
        // 使用@RequestParam注解标记handler方法的形参
        // SpringMVC 会将获取到的请求参数从形参位置给我们传进来
        @RequestParam("userName") String userName
) {
    
    logger.debug("获取到请求参数：" + userName);
    
    return "target";
}
```



#### [2]@RequestParam注解省略的情况

```java
@RequestMapping("/param/one/name/one/value")
public String oneNameOneValue(
        // 当请求参数名和形参名一致，可以省略@RequestParam("userName")注解
        // 但是，省略后代码可读性下降而且将来在SpringCloud中不能省略，所以建议还是不要省略
        String userName
) {
    
    logger.debug("★获取到请求参数：" + userName);
    
    return "target";
}
```



#### [3]必须的参数没有提供

![./images](./images/img008.png)

页面信息说明：

- 响应状态码：400（在 SpringMVC 环境下，400通常和数据注入相关）
- 说明信息：必需的 String 请求参数 'userName' 不存在

原因可以参考 @RequestParam 注解的 required 属性：默认值为true，表示请求参数默认必须提供

```java
	/**
	 * Whether the parameter is required.
	 * <p>Defaults to {@code true}, leading to an exception being thrown
	 * if the parameter is missing in the request. Switch this to
	 * {@code false} if you prefer a {@code null} value if the parameter is
	 * not present in the request.
	 * <p>Alternatively, provide a {@link #defaultValue}, which implicitly
	 * sets this flag to {@code false}.
	 */
	boolean required() default true;
```



#### [4]关闭请求参数必需

required 属性设置为 false 表示这个请求参数可有可无：

```java
@RequestParam(value = "userName", required = false)
```



#### [5]给请求参数设置默认值

使用 defaultValue 属性给请求参数设置默认值：

```java
@RequestParam(value = "userName", required = false, defaultValue = "missing")
```

此时 required 属性可以继续保持默认值：

```java
@RequestParam(value = "userName", defaultValue = "missing")
```



## 2、一名多值

### ①表单

```html
<form th:action="@{/param/one/name/multi/value}" method="post">
    请选择你最喜欢的球队：
    <input type="checkbox" name="team" value="Brazil"/>巴西
    <input type="checkbox" name="team" value="German"/>德国
    <input type="checkbox" name="team" value="French"/>法国
    <input type="checkbox" name="team" value="Holland"/>荷兰
    <input type="checkbox" name="team" value="Italian"/>意大利
    <input type="checkbox" name="team" value="China"/>中国
    <br/>
    <input type="submit" value="保存"/>
</form>
```



### ②handler方法

```java
@RequestMapping("/param/one/name/multi/value")
public String oneNameMultiValue(
    
        // 在服务器端 handler 方法中，使用一个能够存储多个数据的容器就能接收一个名字对应的多个值请求参数
        @RequestParam("team") List<String> teamList
        ) {
    
    for (String team : teamList) {
        logger.debug("team = " + team);
    }
    
    return "target";
}
```



## 3、表单对应模型

### ①表单

```html
<form action="emp/save" method="post">
    姓名：<input type="text" name="empName"/><br/>
    年龄：<input type="text" name="empAge"/><br/>
    工资：<input type="text" name="empSalary"/><br/>
    <input type="submit" value="保存"/>
</form>
```



### ②实体类

```java
public class Employee {
    
    private Integer empId;
    private String empName;
    private int empAge;
    private double empSalary;
    ……
```



### ③handler方法

```java
@RequestMapping("/param/form/to/entity")
public String formToEntity(
    
        // SpringMVC 会自动调用实体类中的 setXxx() 注入请求参数
        Employee employee) {
    
    logger.debug(employee.toString());
    
    return "target";
}
```



### ④POST请求的字符乱码问题

到 web.xml 中配置 CharacterEncodingFilter 即可：

```xml
<!-- 配置过滤器解决 POST 请求的字符乱码问题 -->
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    
    <!-- encoding参数指定要使用的字符集名称 -->
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    
    <!-- 请求强制编码 -->
    <init-param>
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
        
    <!-- 响应强制编码 -->
    <init-param>
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

::: tip

注1：在较低版本的 SpringMVC 中，forceRequestEncoding 属性、forceResponseEncoding 属性没有分开，它们是一个 forceEncoding 属性。这里需要注意一下。



注2：由于 CharacterEncodingFilter 是通过 request.setCharacterEncoding(encoding); 来设置请求字符集，所以在此操作前不能有任何的 request.getParameter() 操作。在设置字符集之前获取过请求参数，那么设置字符集的操作将无效。

:::



## 4、表单对应实体类包含级联属性

### ①实体类

```java
public class Student {
    
    private String stuName;
    private School school;
    private List<Subject> subjectList;
    private Subject[] subjectArray;
    private Set<Teacher> teacherSet;
    private Map<String, Double> scores;
    
    public Student() {
        //在各种常用数据类型中，只有Set类型需要提前初始化
        //并且要按照表单将要提交的对象数量进行初始化
        //Set类型使用非常不便，要尽可能避免使用Set
        teacherSet = new HashSet<>();
        teacherSet.add(new Teacher());
        teacherSet.add(new Teacher());
        teacherSet.add(new Teacher());
        teacherSet.add(new Teacher());
        teacherSet.add(new Teacher());
    }
    ……
```

[其他实体类点这里](verse04/detail01.html)



### ②表单

表单项中的 name 属性值必须严格按照级联对象的属性来设定：

```html
<!-- 提交数据的表单 -->
<form th:action="@{/param/cascad}" method="post">
    stuName：<input type="text" name="stuName" value="tom"/><br/>
    school.schoolName:<input type="text" name="school.schoolName" value="atguigu"/><br/>
    subjectList[0].subjectName:<input type="text" name="subjectList[0].subjectName" value="java"/><br/>
    subjectList[1].subjectName:<input type="text" name="subjectList[1].subjectName" value="php"/><br/>
    subjectList[2].subjectName:<input type="text" name="subjectList[2].subjectName" value="javascript"/><br/>
    subjectList[3].subjectName:<input type="text" name="subjectList[3].subjectName" value="css"/><br/>
    subjectList[4].subjectName:<input type="text" name="subjectList[4].subjectName" value="vue"/><br/>
    subjectArray[0].subjectName:<input type="text" name="subjectArray[0].subjectName" value="spring"/><br/>
    subjectArray[1].subjectName:<input type="text" name="subjectArray[1].subjectName" value="SpringMVC"/><br/>
    subjectArray[2].subjectName:<input type="text" name="subjectArray[2].subjectName" value="mybatis"/><br/>
    subjectArray[3].subjectName:<input type="text" name="subjectArray[3].subjectName" value="maven"/><br/>
    subjectArray[4].subjectName:<input type="text" name="subjectArray[4].subjectName" value="mysql"/><br/>
    tearcherSet[0].teacherName:<input type="text" name="tearcherSet[0].teacherName" value="t_one"/><br/>
    tearcherSet[1].teacherName:<input type="text" name="tearcherSet[1].teacherName" value="t_two"/><br/>
    tearcherSet[2].teacherName:<input type="text" name="tearcherSet[2].teacherName" value="t_three"/><br/>
    tearcherSet[3].teacherName:<input type="text" name="tearcherSet[3].teacherName" value="t_four"/><br/>
    tearcherSet[4].teacherName:<input type="text" name="tearcherSet[4].teacherName" value="t_five"/><br/>
    scores['Chinese']：input type="text" name="scores['Chinese']" value="100"/><br/>
    scores['English']：<input type="text" name="scores['English']" value="95" /><br/>
    scores['Mathematics']：<input type="text" name="scores['Mathematics']" value="88"/><br/>
    scores['Chemistry']：<input type="text" name="scores['Chemistry']" value="63"/><br/>
    scores['Biology']：<input type="text" name="scores['Biology']" value="44"/><br/>
    <input type="submit" value="保存"/>
</form>
```



### ③handler方法

```java
@RequestMapping("/param/form/to/nested/entity")
public String formToNestedEntity(
    
        // SpringMVC 自己懂得注入级联属性，只要属性名和对应的getXxx()、setXxx()匹配即可
        Student student) {
    
    logger.debug(student.toString());
    
    return "target";
}
```



## 5、要发送的数据是 List

### ①额外封装一层

```java
public class EmployeeParam {
    
    private List<Employee> employeeList;
    ……
```



### ②表单

```html
直接发送 List&lt;Employee&gt;：<br/>
<form th:action="@{/param/list/emp}" method="post">
    1号员工姓名：<input type="text" name="employeeList[0].empName" /><br/>
    1号员工年龄：<input type="text" name="employeeList[0].empAge" /><br/>
    1号员工工资：<input type="text" name="employeeList[0].empSalary" /><br/>
    2号员工姓名：<input type="text" name="employeeList[1].empName" /><br/>
    2号员工年龄：<input type="text" name="employeeList[1].empAge" /><br/>
    2号员工工资：<input type="text" name="employeeList[1].empSalary" /><br/>
    <button type="submit">保存</button>
</form>
```



### ③handler方法

```java
@RequestMapping("/param/list/emp")
public String saveEmpList(
        // SpringMVC 访问这里实体类的setEmployeeList()方法注入数据
        EmployeeParam employeeParam
) {
    
    List<Employee> employeeList = employeeParam.getEmployeeList();
    
    for (Employee employee : employeeList) {
        logger.debug(employee.toString());
    }
    
    return "target";
}
```



[上一节](verse03.html) [回目录](index.html) [下一节](verse05.html)

