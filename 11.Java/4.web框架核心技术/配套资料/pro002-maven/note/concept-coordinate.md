[[toc]]

# 坐标

## 1、坐标的作用

在Maven仓库中唯一定位到一个jar包

## 2、坐标中的三个向量

| 向量名称   | 向量作用             | 向量命名规范    |
| ---------- | -------------------- | --------------- |
| groupId    | 定位到公司开发的项目 | 域名倒序.项目名 |
| artifactId | 定位到项目中的模块   | 模块名          |
| version    | 指定模块版本         | 无              |

## 3、坐标对应的jar包路径

坐标：

```xml
  <groupId>javax.servlet</groupId>
  <artifactId>servlet-api</artifactId>
  <version>2.5</version>
```

上面坐标对应的jar包在Maven本地仓库中的位置：

```text
Maven本地仓库根目录\javax\servlet\servlet-api\2.5\servlet-api-2.5.jar
```



[上一节](concept-directory.html) [回目录](index.html) [下一节](concept-life-cycle.html)