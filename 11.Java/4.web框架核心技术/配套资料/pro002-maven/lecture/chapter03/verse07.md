[[toc]]

# 实验七：测试依赖的传递性



## 1、依赖的传递性

### ①概念

A依赖B，B依赖C，那么在A没有配置对C的依赖的情况下，A里面能不能直接使用C？



### ②传递的原则

在A依赖B，B依赖C的前提下，C是否能够传递到A，取决于B依赖C时使用的依赖范围。

- B依赖C时使用compile范围：可以传递
- B依赖C时使用test或provided范围：不能传递，所以需要这样的jar包时，就必须在需要的地方明确配置依赖才可以。



## 2、使用compile范围依赖spring-core

测试方式：让pro01-maven-java工程依赖spring-core

具体操作：编辑pro01-maven-java工程根目录下pom.xml

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-core -->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<version>4.0.0.RELEASE</version>
</dependency>
```

使用mvn dependency:tree命令查看效果：

> [INFO] com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO]    \- commons-logging:commons-logging:jar:1.1.1:compile

还可以在Web工程中，使用mvn dependency:tree命令查看效果（需要重新将pro01-maven-java安装到仓库）：

> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
> [INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO]    \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO]       \- commons-logging:commons-logging:jar:1.1.1:compile



## 3、验证test和provided范围不能传递

从上面的例子已经能够看到，pro01-maven-java依赖了junit，但是在pro02-maven-web工程中查看依赖树的时候并没有看到junit。

要验证provided范围不能传递，可以在pro01-maven-java工程中加入servlet-api的依赖。

```xml
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.1.0</version>
	<scope>provided</scope>
</dependency>
```

效果还是和之前一样：

> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
> [INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO]    \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO]       \- commons-logging:commons-logging:jar:1.1.1:compile



[上一节](verse06.html) [回目录](index.html) [下一节](verse08.html)