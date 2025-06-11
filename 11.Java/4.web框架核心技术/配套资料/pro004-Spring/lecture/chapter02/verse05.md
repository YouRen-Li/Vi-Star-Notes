[[toc]]

# 第五节 基于注解的AOP



## 1、AOP概念介绍

### ①名词解释

AOP：Aspect Oriented Programming面向切面编程



### ②AOP的作用

下面两点是同一件事的两面，一枚硬币的两面：

- 简化代码：把方法中固定位置的重复的代码<span style="color:blue;font-weight:bold;">抽取</span>出来，让被抽取的方法更专注于自己的核心功能，提高内聚性。
- 代码增强：把特定的功能封装到切面类中，看哪里有需要，就往上套，被<span style="color:blue;font-weight:bold;">套用</span>了切面逻辑的方法就被切面给增强了。



## 2、基于注解的AOP用到的技术

![./images](./images/img006.png)

- 动态代理（InvocationHandler）：JDK原生的实现方式，需要被代理的目标类必须实现接口。因为这个技术要求<span style="color:blue;font-weight:bold;">代理对象和目标对象实现同样的接口</span>（兄弟两个拜把子模式）。
- cglib：通过<span style="color:blue;font-weight:bold;">继承被代理的目标类</span>（认干爹模式）实现代理，所以不需要目标类实现接口。
- AspectJ：本质上是静态代理，<span style="color:blue;font-weight:bold;">将代理逻辑“织入”被代理的目标类编译得到的字节码文件</span>，所以最终效果是动态的。weaver就是织入器。Spring只是借用了AspectJ中的注解。



## 3、实验操作

[实验一 初步实现](verse05/experiment01.html)

[实验二 各个通知获取细节信息](verse05/experiment02.html)

[实验三 重用切入点表达式](verse05/experiment03.html)

[实验四 切入点表达式语法](verse05/experiment04.html)

[实验五 环绕通知](verse05/experiment05.html)

[实验六 切面的优先级](verse05/experiment06.html)

[实验七 没有接口的情况](verse05/experiment07.html)



## 4、小结

![./images](./images/img015.png)



[上一节](verse04.html) [回目录](index.html) [下一节](verse06.html)