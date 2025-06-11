[[toc]]

# 第二节 IOC容器概念

## 1、普通容器

### ①生活中的普通容器

![./images](./images/img002.png)

普通容器只能用来存储，没有更多功能。



### ②程序中的普通容器

- 数组
- 集合：List
- 集合：Set



## 2、复杂容器

### ①生活中的复杂容器

![./images](./images/img003.png)

政府管理我们的一生，生老病死都和政府有关。



### ②程序中的复杂容器

Servlet 容器能够管理 Servlet、Filter、Listener 这样的组件的一生，所以它是一个复杂容器。我们即将要学习的 IOC 容器也是一个复杂容器。它们不仅要负责<span style="color:blue;font-weight:bold;">创建</span>组件的对象、<span style="color:blue;font-weight:bold;">存储</span>组件的对象，还要负责<span style="color:blue;font-weight:bold;">调用</span>组件的方法让它们工作，最终在特定情况下<span style="color:blue;font-weight:bold;">销毁</span>组件。



#### [1]Servlet生命周期

| 名称       | 时机                                                         | 次数 |
| ---------- | ------------------------------------------------------------ | ---- |
| 创建对象   | 默认情况：接收到第一次请求<br />修改启动顺序后：Web应用启动过程中 | 一次 |
| 初始化操作 | 创建对象之后                                                 | 一次 |
| 处理请求   | 接收到请求                                                   | 多次 |
| 销毁操作   | Web应用卸载之前                                              | 一次 |



#### [2]Filter生命周期

| 生命周期阶段 | 执行时机         | 执行次数 |
| ------------ | ---------------- | -------- |
| 创建对象     | Web应用启动时    | 一次     |
| 初始化       | 创建对象后       | 一次     |
| 拦截请求     | 接收到匹配的请求 | 多次     |
| 销毁         | Web应用卸载前    | 一次     |



## 3、IOC思想

IOC：Inversion of Control，翻译过来是<span style="color:blue;font-weight:bold;">反转控制</span>。



### ①获取资源的传统方式

自己做饭：买菜、洗菜、择菜、改刀、炒菜，全过程参与，费时费力，必须清楚了解资源创建整个过程中的全部细节且熟练掌握。

在应用程序中的组件需要获取资源时，传统的方式是组件<span style="color:blue;font-weight:bold;">主动</span>的从容器中获取所需要的资源，在这样的模式下开发人员往往需要知道在具体容器中特定资源的获取方式，增加了学习成本，同时降低了开发效率。



### ②反转控制方式获取资源

点外卖：下单、等、吃，省时省力，不必关心资源创建过程的所有细节。

反转控制的思想完全颠覆了应用程序组件获取资源的传统方式：反转了资源的获取方向——改由容器主动的将资源推送给需要的组件，开发人员不需要知道容器是如何创建资源对象的，只需要提供接收资源的方式即可，极大的降低了学习成本，提高了开发的效率。这种行为也称为查找的<span style="color:blue;font-weight:bold;">被动</span>形式。



### ③DI

DI：Dependency Injection，翻译过来是<span style="color:blue;font-weight:bold;">依赖注入</span>。

DI 是 IOC 的另一种表述方式：即组件以一些预先定义好的方式（例如：setter 方法）接受来自于容器的资源注入。相对于IOC而言，这种表述更直接。

所以结论是：IOC 就是一种反转控制的思想， 而 DI 是对 IOC 的一种具体实现。



## 4、 IOC容器在Spring中的实现

Spring 的 IOC 容器就是 IOC 思想的一个落地的产品实现。IOC 容器中管理的组件也叫做 bean。在创建 bean 之前，首先需要创建 IOC 容器。Spring 提供了 IOC 容器的两种实现方式：



### ①BeanFactory

这是 IOC 容器的基本实现，是 Spring 内部使用的接口。面向 Spring 本身，不提供给开发人员使用。



### ②ApplicationContext

BeanFactory 的子接口，提供了更多高级特性。面向 Spring 的使用者，几乎所有场合都使用 ApplicationContext 而不是底层的 BeanFactory。

> 以后在 Spring 环境下看到一个类或接口的名称中包含 ApplicationContext，那基本就可以断定，这个类或接口与 IOC 容器有关。



### ③ApplicationContext的主要实现类

![iamges](./images/img004.png)



| 类型名                          | 简介                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| ClassPathXmlApplicationContext  | 通过读取类路径下的 XML 格式的配置文件创建 IOC 容器对象       |
| FileSystemXmlApplicationContext | 通过文件系统路径读取 XML 格式的配置文件创建 IOC 容器对象     |
| ConfigurableApplicationContext  | ApplicationContext 的子接口，包含一些扩展方法 refresh() 和 close() ，让 ApplicationContext 具有启动、关闭和刷新上下文的能力。 |
| WebApplicationContext           | 专门为 Web 应用准备，基于 Web 环境创建 IOC 容器对象，并将对象引入存入 ServletContext 域中。 |



[上一节](verse01.html) [回目录](index.html) [下一节](verse03.html)