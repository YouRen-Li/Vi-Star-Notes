[[toc]]

# 第四节 AOP术语

## 1、横切关注点

从每个方法中抽取出来的同一类非核心业务。在同一个项目中，我们可以使用多个横切关注点对相关方法进行多个不同方面的增强。

这个概念不是语法层面天然存在的，而是根据附加功能的逻辑上的需要：有十个附加功能，就有十个横切关注点。

![./images](./images/img007.png)



## 2、通知[记住]

每一个横切关注点上要做的事情都需要写一个方法来实现，这样的方法就叫通知方法。

- 前置通知：在被代理的目标方法<span style="color:blue;font-weight:bold;">前</span>执行
- 返回通知：在被代理的目标方法<span style="color:blue;font-weight:bold;">成功结束</span>后执行（<span style="color:blue;font-weight:bold;">寿终正寝</span>）
- 异常通知：在被代理的目标方法<span style="color:blue;font-weight:bold;">异常结束</span>后执行（<span style="color:blue;font-weight:bold;">死于非命</span>）
- 后置通知：在被代理的目标方法<span style="color:blue;font-weight:bold;">最终结束</span>后执行（<span style="color:blue;font-weight:bold;">盖棺定论</span>）
- 环绕通知：使用try...catch...finally结构围绕<span style="color:blue;font-weight:bold;">整个</span>被代理的目标方法，包括上面四种通知对应的所有位置

![./images](./images/img008.png)



## 3、切面[记住]

封装通知方法的类。

![./images](./images/img009.png)



## 4、目标

被代理的目标对象。



## 5、代理

向目标对象应用通知之后创建的代理对象。



## 6、连接点

这也是一个纯逻辑概念，不是语法定义的。

把方法排成一排，每一个横切位置看成x轴方向，把方法从上到下执行的顺序看成y轴，x轴和y轴的交叉点就是连接点。

![./images](./images/img010.png)



## 7、切入点[记住]

定位连接点的方式。

每个类的方法中都包含多个连接点，所以连接点是类中客观存在的事物（从逻辑上来说）。

如果把连接点看作数据库中的记录，那么切入点就是查询记录的 SQL 语句。

Spring 的 AOP 技术可以通过切入点定位到特定的连接点。

切点通过 org.springframework.aop.Pointcut 接口进行描述，它使用类和方法作为连接点的查询条件。



[上一节](verse03.html) [回目录](index.html) [下一节](verse05.html)