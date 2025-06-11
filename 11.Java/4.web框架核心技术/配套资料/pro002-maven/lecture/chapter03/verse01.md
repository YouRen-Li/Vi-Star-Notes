[[toc]]

> 学习命令行操作作为将来在IDEA中使用图形化界面操作的过渡。使用命令行可以不受IDEA这样的环境的干扰，这样就有一个纯净的测试Maven功能的环境。我们的目标是通过命令学习Maven的功能和用法。

# 第一节 实验一：根据坐标创建Maven工程



## 1、Maven核心概念：坐标

### ①数学中的坐标

![./images](./images/img009.png)

使用x、y、z三个“向量”作为空间的坐标系，可以在空间中唯一的定位到一个点。



### ②Maven中的坐标

#### [1]向量说明

使用三个向量在Maven的仓库中唯一的定位到一个jar包。

- <span style="color:blue;font-weight:bold;">groupId</span>：公司或组织的id
- <span style="color:blue;font-weight:bold;">artifactId</span>：一个项目或者是项目中的一个模块的id
- <span style="color:blue;font-weight:bold;">version</span>：版本号

#### [2]三个向量的取值方式

- groupId：公司或组织域名的倒序，通常也会加上项目名称
  - 例如：com.atguigu.maven
- artifactId：模块的名称，将来作为Maven工程的工程名
- version：模块的版本号，根据自己的需要设定
  - 例如：SNAPSHOT表示快照版本，正在迭代过程中，不稳定的版本
  - 例如：RELEASE表示正式版本

举例：

- groupId：com.atguigu.maven
- artifactId：pro01-atguigu-maven
- version：1.0-SNAPSHOT



### ③坐标和仓库中jar包的存储路径之间的对应关系

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

一定要学会根据坐标到本地仓库中找到对应的jar包。

 

## 2、实验操作

### ①创建目录作为后面操作的工作空间

例如：D:\maven-workspace\space210323



### ②在工作空间目录下打开命令行窗口

![./images](./images/img010.png)



### ③使用命令生成Maven工程

运行<span style="color:blue;font-weight:bold;">mvn archetype:generate</span>命令

下面根据提示操作

> Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): 7:【直接回车，使用默认值】
>
> 
>
> Define value for property 'groupId': com.atguigu.maven
>
> Define value for property 'artifactId': pro01-maven-java
>
> Define value for property 'version' 1.0-SNAPSHOT: :【直接回车，使用默认值】
>
> Define value for property 'package' com.atguigu.maven: :【直接回车，使用默认值】
>
> 
>
> Confirm properties configuration:
> groupId: com.atguigu.maven
> artifactId: pro01-maven-java
> version: 1.0-SNAPSHOT
> package: com.atguigu.maven
> Y: :【直接回车，表示确认】



### ④调整

Maven默认生成的工程，对junit依赖的是较低的3.8.1版本，我们可以改成4.12版本。

自动生成的App.java和AppTest.java可以删除。

```xml
<!-- 依赖信息配置 -->
<!-- dependencies复数标签：里面包含dependency单数标签 -->
<dependencies>
	<!-- dependency单数标签：配置一个具体的依赖 -->
	<dependency>
		<!-- 通过坐标来依赖其他jar包 -->
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
		
		<!-- 依赖的范围 -->
		<scope>test</scope>
	</dependency>
</dependencies>
```



## 3、Maven核心概念：POM

### ①含义

POM：<span style="color:blue;font-weight:bold;">P</span>roject <span style="color:blue;font-weight:bold;">O</span>bject <span style="color:blue;font-weight:bold;">M</span>odel，项目对象模型。和POM类似的是：DOM：Document Object Model，文档对象模型。



### ②思想

POM表示将工程抽象为一个模型，再用程序中的对象来描述这个模型。这样我们就可以用程序来管理项目了。我们在开发过程中，最基本的做法就是将现实生活中的事物抽象为模型，然后封装模型相关的数据作为一个对象，这样就可以在程序中计算与现实事物相关的数据。



### ③对应的配置文件

POM理念集中体现在Maven工程根目录下<span style="color:blue;font-weight:bold;">pom.xml</span>这个配置文件中。所以这个pom.xml配置文件就是Maven工程的核心配置文件。其实学习Maven就是学这个文件怎么配置，各个配置有什么用。



## 4、Maven核心概念：约定的目录结构

### ①各个目录的作用

![./images](./images/img011.png)

另外还有一个target目录专门存放构建操作输出的结果。



### ②约定目录结构的意义

Maven为了让构建过程能够尽可能自动化完成，所以必须约定目录结构的作用。例如：Maven执行编译操作，必须先去Java源程序目录读取Java源代码，然后执行编译，最后把编译结果存放在target目录。



### ③约定大于配置

Maven对于目录结构这个问题，没有采用配置的方式，而是基于约定。这样会让我们在开发过程中非常方便。如果每次创建Maven工程后，还需要针对各个目录的位置进行详细的配置，那肯定非常麻烦。

目前开发领域的技术发展趋势就是：约定大于配置，配置大于编码。



[回目录](index.html) [下一节](verse02.html)