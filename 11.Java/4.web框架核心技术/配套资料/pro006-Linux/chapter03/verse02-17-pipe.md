[[toc]]

# 第二节 文件和目录相关命令：管道

## 1、概述

管道不是命令，而是一个符号：“<span style="color:blue;font-weight:bold;font-size:30px;">|</span>”。它的用法是：<span style="color:blue;font-weight:bold;">命令A <span style="color:red;font-size:30px;">|</span> 命令B</span>。作用是把命令A的输出作为命令B的输入。



## 2、举例

### ①需求

显示当前目录下的所有文件。如果使用<span style="color:blue;font-weight:bold;font-family:consolas;">“ll”</span>命令那么文件和目录都会显示出来。

![./images](./images/img052.png)



### ②文件和目录的特征

在显示的详细信息中，文件是以“-”开头的，目录是以“d”开头的。



### ③按照特征编写正则表达式

匹配以“-“开头的行：<span style="color:blue;font-weight:bold;font-size:30px;">^-</span>



### ④完整的命令

![./images](./images/img053.png)



### ⑤工作机制解析

![./images](./images/img054.png)



### ⑥管道可以多重使用

![./images](./images/img055.png)

<span style="color:blue;font-size:25px;font-family:consolas;">wc -l</span>命令可以统计文本数据的行数



[上一条](verse02-16-grep.html) [回目录](verse02-00-index.html) [下一条](verse02-18-tar.html)