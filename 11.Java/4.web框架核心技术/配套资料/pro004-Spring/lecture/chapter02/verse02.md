[[toc]]

# 第二节 代理模式

## 1、概念

### ①介绍

二十三种设计模式中的一种，属于结构型模式。它的作用就是通过提供一个代理类，让我们在调用目标方法的时候，不再是直接对目标方法进行调用，而是通过代理类<span style="color:blue;font-weight:bold;">间接</span>调用。让不属于目标方法核心逻辑的代码从目标方法中剥离出来——<span style="color:blue;font-weight:bold;">解耦</span>。调用目标方法时先调用代理对象的方法，减少对目标方法的调用和打扰，同时让附加功能能够集中在一起也有利于统一维护。

![./images](./images/img004.png)

使用代理后：

![./images](./images/img005.png)



### ②生活中的代理

- 广告商找大明星拍广告需要经过经纪人
- 合作伙伴找大老板谈合作要约见面时间需要经过秘书
- 房产中介是买卖双方的代理



### ③相关术语

- 代理：将非核心逻辑剥离出来以后，封装这些非核心逻辑的类、对象、方法。
- 目标：被代理“套用”了非核心逻辑代码的类、对象、方法。



> 理解代理模式、AOP的核心关键词就一个字：<span style="color:blue;font-weight:bold;">套</span>



## 2、静态代理

创建静态代理类：

```java
public class CalculatorStaticProxy implements Calculator {
    
    // 将被代理的目标对象声明为成员变量
    private Calculator target;
    
    public CalculatorStaticProxy(Calculator target) {
        this.target = target;
    }
    
    @Override
    public int add(int i, int j) {
    
        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);
    
        // 通过目标对象来实现核心业务逻辑
        int addResult = target.add(i, j);
    
        System.out.println("[日志] add 方法结束了，结果是：" + addResult);
    
        return addResult;
    }
    ……
```

静态代理确实实现了解耦，但是由于代码都写死了，完全不具备任何的灵活性。就拿日志功能来说，将来其他地方也需要附加日志，那还得再声明更多个静态代理类，那就产生了大量重复的代码，日志功能还是分散的，没有统一管理。



提出进一步的需求：将日志功能集中到一个代理类中，将来有任何日志需求，都通过这一个代理类来实现。这就需要使用动态代理技术了。



## 3、动态代理

![./images](./images/img003.png)



### ①生产代理对象的工厂类

JDK本身就支持动态代理，这是反射技术的一部分。下面我们还是创建一个代理类（生产代理对象的工厂类）：

```java
// 泛型T要求是目标对象实现的接口类型，本代理类根据这个接口来进行代理
public class LogDynamicProxyFactory<T> {
    
    // 将被代理的目标对象声明为成员变量
    private T target;
    
    public LogDynamicProxyFactory(T target) {
        this.target = target;
    }
    
    public T getProxy() {
    
        // 创建代理对象所需参数一：加载目标对象的类的类加载器
        ClassLoader classLoader = target.getClass().getClassLoader();
    
        // 创建代理对象所需参数二：目标对象的类所实现的所有接口组成的数组
        Class<?>[] interfaces = target.getClass().getInterfaces();
    
        // 创建代理对象所需参数三：InvocationHandler对象
        // Lambda表达式口诀：
        // 1、复制小括号
        // 2、写死右箭头
        // 3、落地大括号
        InvocationHandler handler = (
                                    // 代理对象，当前方法用不上这个对象
                                    Object proxy,
    
                                     // method就是代表目标方法的Method对象
                                     Method method,
    
                                     // 外部调用目标方法时传入的实际参数
                                     Object[] args)->{
    
            // 我们对InvocationHandler接口中invoke()方法的实现就是在调用目标方法
            // 围绕目标方法的调用，就可以添加我们的附加功能
    
            // 声明一个局部变量，用来存储目标方法的返回值
            Object targetMethodReturnValue = null;
    
            // 通过method对象获取方法名
            String methodName = method.getName();
    
            // 为了便于在打印时看到数组中的数据，把参数数组转换为List
            List<Object> argumentList = Arrays.asList(args);
    
            try {
    
                // 在目标方法执行前：打印方法开始的日志
                System.out.println("[动态代理][日志] " + methodName + " 方法开始了，参数是：" + argumentList);
    
                // 调用目标方法：需要传入两个参数
                // 参数1：调用目标方法的目标对象
                // 参数2：外部调用目标方法时传入的实际参数
                // 调用后会返回目标方法的返回值
                targetMethodReturnValue = method.invoke(target, args);
    
                // 在目标方法成功后：打印方法成功结束的日志【寿终正寝】
                System.out.println("[动态代理][日志] " + methodName + " 方法成功结束了，返回值是：" + targetMethodReturnValue);
    
            }catch (Exception e){
    
                // 通过e对象获取异常类型的全类名
                String exceptionName = e.getClass().getName();
    
                // 通过e对象获取异常消息
                String message = e.getMessage();
    
                // 在目标方法失败后：打印方法抛出异常的日志【死于非命】
                System.out.println("[动态代理][日志] " + methodName + " 方法抛异常了，异常信息是：" + exceptionName + "," + message);
    
            }finally {
    
                // 在目标方法最终结束后：打印方法最终结束的日志【盖棺定论】
                System.out.println("[动态代理][日志] " + methodName + " 方法最终结束了");
    
            }
    
            // 这里必须将目标方法的返回值返回给外界，如果没有返回，外界将无法拿到目标方法的返回值
            return targetMethodReturnValue;
        };
    
        // 创建代理对象
        T proxy = (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    
        // 返回代理对象
        return proxy;
    }
}
```



### ②测试

```java
@Test
public void testDynamicProxy() {
    
    // 1.创建被代理的目标对象
    Calculator target = new CalculatorPureImpl();
    
    // 2.创建能够生产代理对象的工厂对象
    LogDynamicProxyFactory<Calculator> factory = new LogDynamicProxyFactory<>(target);
    
    // 3.通过工厂对象生产目标对象的代理对象
    Calculator proxy = factory.getProxy();
    
    // 4.通过代理对象间接调用目标对象
    int addResult = proxy.add(10, 2);
    System.out.println("方法外部 addResult = " + addResult + "\n");
    
    int subResult = proxy.sub(10, 2);
    System.out.println("方法外部 subResult = " + subResult + "\n");
    
    int mulResult = proxy.mul(10, 2);
    System.out.println("方法外部 mulResult = " + mulResult + "\n");
    
    int divResult = proxy.div(10, 2);
    System.out.println("方法外部 divResult = " + divResult + "\n");
}
```



### ③练习

动态代理的实现过程不重要，重要的是使用现成的动态代理类去<span style="color:blue;font-weight:bold;">套</span>用到其他目标对象上。

声明另外一个接口：

```java
public interface SoldierService {
    
    int saveSoldier(String soldierName);
    
    int removeSoldier(Integer soldierId);
    
    int updateSoldier(Integer soldierId, String soldierName);
    
    String getSoldierNameById(Integer soldierId);
    
}
```



给接口一个实现类：

```java
public class SoldierServiceImpl implements SoldierService {
    
    @Override
    public int saveSoldier(String soldierName) {
    
        System.out.println("核心业务逻辑：保存到数据库……");
    
        return 1;
    }
    
    @Override
    public int removeSoldier(Integer soldierId) {
    
        System.out.println("核心业务逻辑：从数据库删除……");
    
        return 1;
    }
    
    @Override
    public int updateSoldier(Integer soldierId, String soldierName) {
    
        System.out.println("核心业务逻辑：更新……");
    
        return 1;
    }

    @Override
    public String getSoldierNameById(Integer soldierId) {
    
        System.out.println("核心业务逻辑：查询数据库……");
    
        return "good";
    }
}
```



测试：

```java
@Test
public void testSoldierServiceDynamicProxy() {
    
    // 1.创建被代理的目标对象
    SoldierService soldierService = new SoldierServiceImpl();
    
    // 2.创建生产代理对象的工厂对象
    LogDynamicProxyFactory<SoldierService> factory = new LogDynamicProxyFactory<>(soldierService);
    
    // 3.生产代理对象
    SoldierService proxy = factory.getProxy();
    
    // 4.通过代理对象调用目标方法
    String soldierName = proxy.getSoldierNameById(1);
    System.out.println("soldierName = " + soldierName);
    
}
```



[上一节](verse01.html) [回目录](index.html) [下一节](verse03.html)