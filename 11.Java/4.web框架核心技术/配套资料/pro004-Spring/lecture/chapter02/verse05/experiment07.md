[[toc]]

# 实验七 没有接口的情况

在目标类没有实现任何接口的情况下，Spring会自动使用cglib技术实现代理。为了证明这一点，我们做下面的测试：

## 1、创建目标类

请确保这个类在自动扫描的包下，同时确保切面的切入点表达式能够覆盖到类中的方法。

```java
@Service
public class EmployeeService {
    
    public void getEmpList() {
        System.out.println("方法内部 com.atguigu.aop.imp.EmployeeService.getEmpList");
    }
    
}
```



## 2、测试

```java
    @Autowired
    private EmployeeService employeeService;
    
    @Test
    public void testNoInterfaceProxy() {
        employeeService.getEmpList();
        System.out.println();
    }
```



## 3、Debug查看

### ①没有实现接口情况

![images](../images/img029.png)



### ②有实现接口的情况

![images](../images/img030.png)



同时我们发现：Mybatis调用的Mapper接口类型的对象其实也是动态代理机制
![images](../images/img031.png)



[上一个实验](experiment06.html) [回目录](../verse05.html)
