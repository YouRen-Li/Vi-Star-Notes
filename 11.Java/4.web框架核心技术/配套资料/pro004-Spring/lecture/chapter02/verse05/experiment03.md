[[toc]]

# 实验三 重用切入点表达式

## 1、声明

在一处声明切入点表达式之后，其他有需要的地方引用这个切入点表达式。易于维护，一处修改，处处生效。声明方式如下：

```java
    // 切入点表达式重用
    @Pointcut("execution(* *..*.add(..))")
    public void declarPointCut() {}
```



## 2、同一个类内部引用

```java
    @Before(value = "declarPointCut()")
    public void printLogBeforeCoreOperation(JoinPoint joinPoint) {
```



## 3、在不同类中引用

```java
@Around(value = "com.atguigu.spring.aop.aspect.LogAspect.declarPointCut()")
public Object roundAdvice(ProceedingJoinPoint joinPoint) {
```



## 4、集中管理

而作为存放切入点表达式的类，可以把整个项目中所有切入点表达式全部集中过来，便于统一管理：

```java
@Component
public class AtguiguPointCut {
    
    @Pointcut(value = "execution(public int *..Calculator.sub(int,int))")
    public void atguiguGlobalPointCut(){}
    
    @Pointcut(value = "execution(public int *..Calculator.add(int,int))")
    public void atguiguSecondPointCut(){}
    
    @Pointcut(value = "execution(* *..*Service.*(..))")
    public void transactionPointCut(){}
}
```



[上一个实验](experiment02.html) [回目录](../verse05.html) [下一个实验](experiment04.html)