[[toc]]

# 实验十：聚合



## 1、聚合本身的含义

部分组成整体

![./images](./images/img029.jpg)

动画片《战神金刚》中的经典台词：“我来组成头部！我来组成手臂！”就是聚合关系最生动的体现。



## 2、Maven中的聚合

使用一个“总工程”将各个“模块工程”汇集起来，作为一个整体对应完整的项目。

- 项目：整体
- 模块：部分

> 概念的对应关系：
>
> 从继承关系角度来看：
>
> - 父工程
> - 子工程

> 从聚合关系角度来看：
>
> - 总工程
> - 模块工程



## 3、好处

- 一键执行Maven命令：很多构建命令都可以在“总工程”中一键执行。

  以mvn install命令为例：Maven要求有父工程时先安装父工程；有依赖的工程时，先安装依赖的工程。我们自己考虑这些规则会很麻烦。但是工程聚合之后，在总工程执行mvn install可以一键完成安装，而且会自动按照正确的顺序执行。

- 配置聚合之后，各个模块工程会在总工程中展示一个列表，让项目中的各个模块一目了然。



## 4、聚合的配置

在总工程中配置modules即可：

```xml
	<modules>  
		<module>pro04-maven-module</module>
		<module>pro05-maven-module</module>
		<module>pro06-maven-module</module>
	</modules>
```



## 5、依赖循环问题

如果A工程依赖B工程，B工程依赖C工程，C工程又反过来依赖A工程，那么在执行构建操作时会报下面的错误：

> [ERROR] [ERROR] The projects in the reactor contain a cyclic reference:

这个错误的含义是：循环引用。



[上一节](verse09.html) [回目录](index.html)