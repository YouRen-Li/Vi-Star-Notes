[[toc]]

> Spring Framework 从 2017 年 9 月推出了 5.0.0 正式版，在原来 4 版本基础上做出了大量新的改进。整个 Spring5 框架的代码<span style="color:blue;font-weight:bold;">基于 Java 8</span>，运行时<span style="color:blue;font-weight:bold;">兼容 JDK 9</span>，许多不建议使用的类和方法在代码库中删除。



# 第一节 JSR305标准相关注解

## 1、从JSR说起

### ①JCP

JCP（Java Community Process) 是一个由SUN公司发起的，开放的国际组织。主要由Java开发者以及被授权者组成，负责Java技术规范维护，Java技术发展和更新。

JCP官网地址：https://jcp.org/en/home/index

![./images](./images/logo2.gif)



### ②JSR

JSR 的全称是：Java Specification Request，意思是 Java 规范提案。谁向谁提案呢？任何人都可以向 JCP (Java Community Process) 提出新增一个标准化技术规范的正式请求。JSR已成为Java界的一个重要标准。登录[ JCP 官网](https://jcp.org/en/home/index)可以查看[所有 JSR 标准](https://jcp.org/en/jsr/all)。



## 2、JSR 305

JSR 305: Annotations for Software Defect Detection

This JSR will work to develop standard annotations (such as @NonNull) that can be applied to Java programs to assist tools that detect software defects.

主要功能：使用注解（例如@NonNull等等）协助开发者侦测软件缺陷。


Spring 从 5.0 版本开始支持了 JSR 305 规范中涉及到的相关注解。

```java
package org.springframework.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;

/**
 * A common Spring annotation to declare that annotated elements cannot be {@code null}.
 *
 * <p>Leverages JSR-305 meta-annotations to indicate nullability in Java to common
 * tools with JSR-305 support and used by Kotlin to infer nullability of Spring API.
 *
 * <p>Should be used at parameter, return value, and field level. Method overrides should
 * repeat parent {@code @NonNull} annotations unless they behave differently.
 *
 * <p>Use {@code @NonNullApi} (scope = parameters + return values) and/or {@code @NonNullFields}
 * (scope = fields) to set the default behavior to non-nullable in order to avoid annotating
 * your whole codebase with {@code @NonNull}.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @since 5.0
 * @see NonNullApi
 * @see NonNullFields
 * @see Nullable
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierNickname
public @interface NonNull {
}
```



## 3、相关注解

| 注解名称       | 含义                     | 可标记位置                                                   |
| -------------- | ------------------------ | ------------------------------------------------------------ |
| @Nullable      | 可以为空                 | @Target({ElementType.<span style="color:blue;font-weight:bold;">METHOD</span>, ElementType.<span style="color:blue;font-weight:bold;">PARAMETER</span>, ElementType.<span style="color:blue;font-weight:bold;">FIELD</span>}) |
| @NonNull       | 不应为空                 | @Target({ElementType.<span style="color:blue;font-weight:bold;">METHOD</span>, ElementType.<span style="color:blue;font-weight:bold;">PARAMETER</span>, ElementType.<span style="color:blue;font-weight:bold;">FIELD</span>}) |
| @NonNullFields | 在特定包下的字段不应为空 | @Target(ElementType.<span style="color:blue;font-weight:bold;">PACKAGE</span>)<br />@TypeQualifierDefault(ElementType.<span style="color:blue;font-weight:bold;">FIELD</span>) |
| @NonNullApi    | 参数和方法返回值不应为空 | @Target(ElementType.<span style="color:blue;font-weight:bold;">PACKAGE</span>)<br />@TypeQualifierDefault({ElementType.<span style="color:blue;font-weight:bold;">METHOD</span>, ElementType.<span style="color:blue;font-weight:bold;">PARAMETER</span>}) |



[回目录](index.html) [下一节](verse02.html)
