[[toc]]

# 依赖

## 1、依赖的基本配置方式

核心：使用坐标依赖需要的jar包。

配置方式：在dependencies标签内配置dependency

```xml
<dependency>
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro01-maven-java</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```



这里需要注意：我们自己开发的工程需要安装到本地仓库才能依赖上

![./images](./images/img001.png)

## 2、依赖的范围

标签的位置：dependencies/dependency/<span style="color:blue;font-weight:bold;">scope</span>

标签的可选值：compile/test/provided



### ①compile和test对比

|         | main目录（空间） | test目录（空间） | 开发过程（时间） | 部署到服务器（时间） |
| ------- | ---------------- | ---------------- | ---------------- | -------------------- |
| compile | 有效             | 有效             | 有效             | 有效                 |
| test    | 无效             | 有效             | 有效             | 无效                 |



### ②compile和provided对比

|          | main目录（空间） | test目录（空间） | 开发过程（时间） | 部署到服务器（时间） |
| -------- | ---------------- | ---------------- | ---------------- | -------------------- |
| compile  | 有效             | 有效             | 有效             | 有效                 |
| provided | 有效             | 有效             | 有效             | 无效                 |



### ③结论

compile：用于<span style="color:blue;font-weight:bold;">主体功能</span>。通常使用的第三方框架的jar包这样在项目实际运行时真正要用到的jar包都是以compile范围进行依赖的。比如SSM框架所需jar包。<br/>

test：用于<span style="color:blue;font-weight:bold;">测试功能</span>。测试过程中使用的jar包，以test范围依赖进来。比如junit。<br/>

provided：用于<span style="color:blue;font-weight:bold;">在开发过程中使用服务器上的jar包</span>。在开发过程中需要用到的“服务器上的jar包”通常以provided范围依赖进来。比如servlet-api、jsp-api。而这个范围的jar包之所以不参与部署、不放进war包，就是避免和服务器上已有的同类jar包产生冲突，同时减轻服务器的负担。说白了就是：“<span style="color:blue;font-weight:bold;">服务器上已经有了，你就别带啦</span>！”<br/>



## 3、依赖的传递性

### ①概念

A依赖B，B依赖C，那么在A没有配置对C的依赖的情况下，A里面是不是可以直接使用C。



### ②传递的原则

在A依赖B，B依赖C的前提下，C是否能够传递到A取决于B依赖C时使用的依赖范围。

- B依赖C时使用compile范围：可以传递
- B依赖C时使用test或provided范围：不能传递，所以需要这样的jar包时，就必须在需要的地方明确配置依赖才可以。



## 4、依赖的排除

### ①概念

当A依赖B，B依赖C而且C可以传递到A的时候，但是A不想要C，需要在A里面把C排除掉。而往往这种情况都是为了避免jar包之间的冲突。

![./images](./images/img027.png)

所以配置依赖的排除其实就是阻止某些jar包的传递。因为这样的jar包传递过来会和其他jar包冲突。



### ②配置方式

```xml
<dependency>
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro01-maven-java</artifactId>
	<version>1.0-SNAPSHOT</version>
	<scope>compile</scope>
	<!-- 使用excludes标签配置依赖的排除	-->
	<exclusions>
		<!-- 在exclude标签中配置一个具体的排除 -->
		<exclusion>
			<!-- 指定要排除的依赖的坐标（不需要写version） -->
			<groupId>commons-logging</groupId>
			<artifactId>commons-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```



[上一节](concept-plugin-goal.html) [回目录](index.html) [下一节](concept-inherit.html)