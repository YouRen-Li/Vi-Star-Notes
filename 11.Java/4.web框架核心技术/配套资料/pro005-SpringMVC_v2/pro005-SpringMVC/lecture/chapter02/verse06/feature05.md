[TOC]

# 具体功能五：执行保存

## 1、流程图

![images](../images/img027.png)



## 2、handler 方法

```java
@RequestMapping("/save/movie")
public String saveMovie(
        
        // 表单提交的请求参数会通过实体类的setXx()方法注入
        Movie movie) {
    
    movieService.saveMovie(movie);
        
    return "redirect:/show/list";
}
```



[上一个功能](feature04.html) [回目录](../verse06.html) [下一个功能](feature06.html)