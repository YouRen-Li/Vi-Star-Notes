[TOC]

# 具体功能四：添加的表单

## 1、流程图

![images](../images/img026.png)



## 2、超链接

在表格中判断列表数据是否存在有两个分支，这两个分支都应该有前往添加页面的超链接：

```html
<tbody th:if="${#lists.isEmpty(movieList)}">
    <tr>
        <td colspan="5">抱歉！没有查询到数据！</td>
    </tr>
    <tr>
        <td colspan="5">
            <a th:href="@{/add/movie/page}">跳转到添加数据的表单页面</a>
        </td>
    </tr>
</tbody>
<tbody th:if="${not #lists.isEmpty(movieList)}">
    <tr th:each="movie : ${movieList}">
        <td th:text="${movie.movieId}">这里显示电影ID</td>
        <td th:text="${movie.movieName}">这里显示电影名称</td>
        <td th:text="${movie.moviePrice}">这里显示电影价格</td>
        <td>
            <a th:href="@{/remove/movie(movieId=${movie.movieId})}">删除</a>
        </td>
        <td>更新</td>
    </tr>
    <tr>
        <td colspan="5">
            <a th:href="@{/add/movie/page}">跳转到添加数据的表单页面</a>
        </td>
    </tr>
</tbody>
```



## 3、配置view-controller

```xml
<mvc:view-controller path="/add/movie/page" view-name="movie-add"/>
```



## 4、准备表单页面

```html
<form th:action="@{/save/movie}" method="post">
    
    电影名称：<input type="text" name="movieName" /><br/>
    电影票价格：<input type="text" name="moviePrice" /><br/>
    
    <button type="submit">保存</button>
    
</form>
```



[上一个功能](feature03.html) [回目录](../verse06.html) [下一个功能](feature05.html)