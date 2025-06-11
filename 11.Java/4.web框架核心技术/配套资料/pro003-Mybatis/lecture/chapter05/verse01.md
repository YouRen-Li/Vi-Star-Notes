[[toc]]

# 第一节 简介



理解缓存的工作机制和缓存的用途。

## 1、缓存机制介绍

![./images](./images/img002.png)



## 2、一级缓存和二级缓存

### ①使用顺序

![./images](./images/img003.png)

查询的顺序是：

- 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用。
- 如果二级缓存没有命中，再查询一级缓存
- 如果一级缓存也没有命中，则查询数据库
- SqlSession关闭之前，一级缓存中的数据会写入二级缓存



### ②效用范围

- 一级缓存：SqlSession级别
- 二级缓存：SqlSessionFactory级别

![./images](./images/img004.png)



它们之间范围的大小参考下面图：

![./images](./images/img005.png)



[回目录](index.html) [下一节](verse02.html)