[[toc]]

# 第三节 下载失败的第一种情况



## 1、Maven的行为描述

- jar包在下载过程中，jar包的扩展名是xxx.jar.lastUpdated
- 网络连接丢失，无法继续下载
- jar包没有下载完，lastUpdated扩展名不会被删除
- 当我们要求Maven重新下载时，Maven看到这个jar包的扩展名是lastUpdated，Maven就不管了



## 2、解决办法

手动删除所有以lastUpdated结尾的文件，然后让Maven重新下载。可是当仓库中lastUpdated文件太多的时候，手动删除不可行，所以我们需要借助批处理文件：clearLastUpdated.bat。使用方法：

- 将clearLastUpdated.bat文件复制到本地仓库的根目录
- 使用文件编辑打开clearLastUpdated.bat文件
- 修改下面的内容

> SET CLEAR_PATH=设置为本地仓库所在的盘符
> SET CLEAR_DIR=设置为本地仓库的根目录的路径

例如：

> SET CLEAR_PATH=D: 
> SET CLEAR_DIR=D:\maven-rep1026

- 在想要执行清理时，双击运行这个文件即可



## 3、要求Maven重新下载jar包

![./images](./images/img043.png)



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)