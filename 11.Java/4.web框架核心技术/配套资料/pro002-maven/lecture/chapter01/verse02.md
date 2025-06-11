[[toc]]

# 第二节 什么是Maven？



Maven是Apache软件基金会组织维护的一款专门为Java项目提供<span style="color:blue;font-weight:bold;">构建</span>和<span style="color:blue;font-weight:bold;">依赖</span>管理支持的工具。

![./images](./images/maven-logo-black-on-white.png)



## 1、构建

Java项目开发过程中，构建指的是使用<span style="color:blue;font-weight:bold;">『原材料生产产品』</span>的过程。

- 原材料

  - Java源代码

  - 基于HTML的Thymeleaf文件

  - 图片

  - 配置文件

  - ###### ……

- 产品

  - 一个可以在服务器上运行的项目

构建过程包含的主要的环节：

- 清理：删除上一次构建的结果，为下一次构建做好准备
- 编译：Java源程序编译成*.class字节码文件
- 测试：运行提前准备好的测试程序
- 报告：针对刚才测试的结果生成一个全面的信息
- 打包
  - Java工程：jar包
  - Web工程：war包
- 安装：把一个Maven工程安装到Maven仓库
- 部署：将准备好的jar包或war包部署到服务器上运行



## 2、依赖

如果A工程里面用到了B工程的类、接口、配置文件等等这样的资源，那么我们就可以说A依赖B。例如：

- junit-4.12依赖hamcrest-core-1.3
- thymeleaf-3.0.12.RELEASE依赖ognl-3.1.26
  - ognl-3.1.26依赖javassist-3.20.0-GA
- thymeleaf-3.0.12.RELEASE依赖attoparser-2.0.5.RELEASE
- thymeleaf-3.0.12.RELEASE依赖unbescape-1.1.6.RELEASE
- thymeleaf-3.0.12.RELEASE依赖slf4j-api-1.7.26

依赖管理中要解决的具体问题：

- jar包的下载：使用Maven之后，jar包会从规范的远程仓库下载到本地
- jar包之间的依赖：通过依赖的传递性自动完成
- jar包之间的冲突：通过对依赖的配置进行调整，让某些jar包不会被导入



## 3、Maven的工作机制

![./images](./images/img003.png)



[上一节](verse01.html) [回目录](index.html)