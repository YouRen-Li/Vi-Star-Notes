[[toc]]

# 第二节 文件和目录相关命令：tail

## 1、简介

命令：<span style="color:blue;font-size:25px;font-family:consolas;">tail</span>

效果：显示文件末尾的部分内容



## 2、基本用法

查看文件末尾5行的内容：

![./images](./images/img038.png)



## 3、高级用法

使用<span style="color:blue;font-size:25px;font-family:consolas;">-F</span>参数实时查看文件末尾新增的内容：

![./images](./images/img039.png)

新开一个窗口编辑这个文件：

![./images](./images/img040.png)

![./images](./images/img041.png)

用vim打开tail命令正在监控的文件：

![./images](./images/img042.png)

在文件末尾新增内容并保存：

![./images](./images/img043.png)

此时在tail命令所在的窗口下查看实时监控：

![./images](./images/img044.png)

使用<span style="color:blue;font-size:25px;font-family:consolas;">Ctrl+c</span>退出实时监控。



[上一条](verse02-12-less.html) [回目录](verse02-00-index.html) [下一条](verse02-14-find.html)