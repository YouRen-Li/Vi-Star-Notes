[[toc]]



# 实验四 切入点表达式语法

## 1、切入点表达式的作用

![images](../images/img028.png)





## 2、语法细节

- 用*号代替“权限修饰符”和“返回值”部分表示“权限修饰符”和“返回值”不限
- 在包名的部分，一个“*”号只能代表包的层次结构中的一层，表示这一层是任意的。
  - 例如：*.Hello匹配com.Hello，不匹配com.atguigu.Hello
- 在包名的部分，使用“*..”表示包名任意、包的层次深度任意
- 在类名的部分，类名部分整体用*号代替，表示类名任意
- 在类名的部分，可以使用*号代替类名的一部分

```java
*Service
```

上面例子表示匹配所有名称以Service结尾的类或接口



- 在方法名部分，可以使用*号表示方法名任意
- 在方法名部分，可以使用*号代替方法名的一部分

```java
*Operation
```

上面例子表示匹配所有方法名以Operation结尾的方法



- 在方法参数列表部分，使用(..)表示参数列表任意
- 在方法参数列表部分，使用(int,..)表示参数列表以一个int类型的参数开头
- 在方法参数列表部分，基本数据类型和对应的包装类型是不一样的
  - 切入点表达式中使用 int 和实际方法中 Integer 是不匹配的
- 在方法返回值部分，如果想要明确指定一个返回值类型，那么必须同时写明权限修饰符

```java
execution(public int *..*Service.*(.., int))
```

上面例子是对的，下面例子是错的：

```java
execution(* int *..*Service.*(.., int))
```

但是public *表示权限修饰符明确，返回值任意是可以的。



- 对于execution()表达式整体可以使用三个逻辑运算符号
  - execution() || execution()表示满足两个execution()中的任何一个即可
  - execution() && execution()表示两个execution()表达式必须都满足
  - !execution()表示不满足表达式的其他方法



## 3、总结

![images](../images/img011.png)



[上一个实验](experiment03.html) [回目录](../verse05.html) [下一个实验](experiment05.html)