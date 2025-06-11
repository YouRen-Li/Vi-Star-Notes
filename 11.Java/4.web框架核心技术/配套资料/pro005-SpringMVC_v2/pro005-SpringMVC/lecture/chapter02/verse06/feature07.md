[TOC]

# 具体功能七：执行更新

## 1、流程图

![images](../images/img029.png)



## 2、handler 方法

```java
@RequestMapping("/update/movie")
public String updateMovie(Movie movie) {
    
    movieService.updateMovie(movie);
    
    return "redirect:/show/list";
}
```



[上一个功能](feature06.html) [回目录](../verse06.html)