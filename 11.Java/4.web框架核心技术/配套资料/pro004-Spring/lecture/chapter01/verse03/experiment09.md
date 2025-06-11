[[toc]]



# 实验九 给bean的属性赋值：特殊值处理



## 1、声明一个类用于测试

```java
package com.atguigu.ioc.component;
    
public class PropValue {
    
    private String commonValue;
    private String expression;
    
    public String getCommonValue() {
        return commonValue;
    }
    
    public void setCommonValue(String commonValue) {
        this.commonValue = commonValue;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    @Override
    public String toString() {
        return "PropValue{" +
                "commonValue='" + commonValue + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }

    public PropValue(String commonValue, String expression) {
        this.commonValue = commonValue;
        this.expression = expression;
    }

    public PropValue() {
    }
}
```



## 2、字面量

### ①用Java代码举例说明

字面量是相对于变量来说的。看下面的代码：

```java
int a = 10;
```

声明一个变量a，初始化为10，此时a就不代表字母a了，而是作为一个变量的名字。当我们引用a的时候，我们实际上拿到的值是10。



而如果a是带引号的：'a'，那么它现在不是一个变量，它就是代表a这个字母本身，这就是字面量。所以字面量没有引申含义，就是我们看到的这个数据本身。



### ②Spring配置文件中举例

#### [1]字面量举例

```xml
<!-- 使用value属性给bean的属性赋值时，Spring会把value属性的值看做字面量 -->
<property name="commonValue" value="hello"/>
```



#### [2]类似变量举例

```xml
<!-- 使用ref属性给bean的属性复制是，Spring会把ref属性的值作为一个bean的id来处理 -->
<!-- 此时ref属性的值就不是一个普通的字符串了，它应该是一个bean的id -->
<property name="happyMachine" ref="happyMachine"/>
```



## 3、null值

```xml
        <property name="commonValue">
            <!-- null标签：将一个属性值明确设置为null -->
            <null/>
        </property>
```



## 4、XML实体

```xml
<!-- 实验九 给bean的属性赋值：特殊值处理 -->
<bean id="propValue" class="com.atguigu.ioc.component.PropValue">
    <!-- 小于号在XML文档中用来定义标签的开始，不能随便使用 -->
    <!-- 解决方案一：使用XML实体来代替 -->
    <property name="expression" value="a &lt; b"/>
</bean>
```



## 5、CDATA节

```xml
<!-- 实验九 给bean的属性赋值：特殊值处理 -->
<bean id="propValue" class="com.atguigu.ioc.component.PropValue">
    <property name="expression">
        <!-- 解决方案二：使用CDATA节 -->
        <!-- CDATA中的C代表Character，是文本、字符的含义，CDATA就表示纯文本数据 -->
        <!-- XML解析器看到CDATA节就知道这里是纯文本，就不会当作XML标签或属性来解析 -->
        <!-- 所以CDATA节中写什么符号都随意 -->
        <value><![CDATA[a < b]]></value>
    </property>
</bean>
```



[上一个实验](experiment08.html) [回目录](../verse03.html) [下一个实验](experiment10.html)