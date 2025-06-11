[[toc]]

# 第七节 AOP对获取bean的影响

# 一、根据类型获取bean

## 1、情景一

- bean对应的类没有实现任何接口
- 根据bean本身的类型获取bean
  - 测试：IOC容器中同类型的bean只有一个
  
    正常获取到IOC容器中的那个bean对象
  
  - 测试：IOC容器中同类型的bean有多个
  
    会抛出NoUniqueBeanDefinitionException异常，表示IOC容器中这个类型的bean有多个



## 2、情景二

- bean对应的类实现了接口，这个接口也只有这一个实现类
  - 测试：根据接口类型获取bean
  - 测试：根据类获取bean
  - 结论：上面两种情况其实都能够正常获取到bean，而且是同一个对象



## 3、情景三

- 声明一个接口
- 接口有多个实现类
- 接口所有实现类都放入IOC容器
  - 测试：根据接口类型获取bean
  
    会抛出NoUniqueBeanDefinitionException异常，表示IOC容器中这个类型的bean有多个
  
  - 测试：根据类获取bean
  
    正常



## 4、情景四

- 声明一个接口
- 接口有一个实现类
- 创建一个切面类，对上面接口的实现类应用通知
  - 测试：根据接口类型获取bean
  - 测试：根据类获取bean



原因分析：

- 应用了切面后，真正放在IOC容器中的是代理类的对象
- 目标类并没有被放到IOC容器中，所以根据目标类的类型从IOC容器中是找不到的

![./images](./images/img021.png)



从内存分析的角度来说，IOC容器中引用的是代理对象，代理对象引用的是目标对象。IOC容器并没有直接引用目标对象，所以根据目标类本身在IOC容器范围内查找不到。

![./images](./images/img022.png)



debug查看代理类的类型：

![./images](./images/img025.png)



## 5、情景五

- 声明一个类
- 创建一个切面类，对上面的类应用通知
  - 测试：根据类获取bean，能获取到

![./images](./images/img023.png)



debug查看实际类型：

![./images](./images/img024.png)



# 二、自动装配

自动装配需先从IOC容器中获取到唯一的一个bean才能够执行装配。所以装配能否成功和装配底层的原理，和前面测试的获取bean的机制是一致的。



## 1、情景一

- 目标bean对应的类没有实现任何接口
- 根据bean本身的类型装配这个bean
  - 测试：IOC容器中同类型的bean只有一个
  
    正常装配
  
  - 测试：IOC容器中同类型的bean有多个
  
    会抛出NoUniqueBeanDefinitionException异常，表示IOC容器中这个类型的bean有多个



## 2、情景二

- 目标bean对应的类实现了接口，这个接口也只有这一个实现类
  - 测试：根据接口类型装配bean
  
    正常
  
  - 测试：根据类装配bean
  
    正常



## 3、情景三

- 声明一个接口
- 接口有多个实现类
- 接口所有实现类都放入IOC容器
  - 测试：根据接口类型装配bean
  
    @Autowired注解会先根据类型查找，此时会找到多个符合的bean，然后根据成员变量名作为bean的id进一步筛选，如果没有id匹配的，则会抛出NoUniqueBeanDefinitionException异常，表示IOC容器中这个类型的bean有多个
  
  - 测试：根据类装配bean
  
    正常



## 4、情景四

- 声明一个接口
- 接口有一个实现类
- 创建一个切面类，对上面接口的实现类应用通知
  - 测试：根据接口类型装配bean
  
    正常
  
  - 测试：根据类装配bean
  
    此时获取不到对应的bean，所以无法装配，抛出下面的异常：

> Caused by: org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'fruitApple' is expected to be of type 'com.atguigu.bean.impl.FruitAppleImpl' but was actually of type 'com.sun.proxy.$Proxy15'



## 5、情景五

- 声明一个类
- 创建一个切面类，对上面的类应用通知
  - 测试：根据类装配bean
  
    正常



# 三、总结

## 1、对实现了接口的类应用切面

![./images](./images/img032.png)



## 2、对没实现接口的类应用切面

![./images](./images/img033.png)



[上一节](verse06.html) [回目录](index.html)