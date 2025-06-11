[[toc]]



# 实验十一 给bean的属性赋值：集合属性

## 1、给组件类添加属性

![images](../images/img012.png)



## 2、配置

```xml
<!-- 实验十三 集合类型的bean -->
<bean id="happyTeam2" class="com.atguigu.ioc.component.HappyTeam">
    <property name="memberList">
        <list>
            <value>member01</value>
            <value>member02</value>
            <value>member03</value>
        </list>
    </property>
</bean>
```



## 3、测试

```java
@Test
public void testExperiment13() {
    
    HappyTeam happyTeam2 = (HappyTeam) iocContainer.getBean("happyTeam2");
    
    List<String> memberList = happyTeam2.getMemberList();
    
    for (String member : memberList) {
        System.out.println("member = " + member);
    }
    
}
```



## 4、其他变化形式

```xml
<!-- 实验十一 给bean的属性赋值：集合属性 -->
<bean id="happyTeam2" class="com.atguigu.ioc.component.HappyTeam">
    <property name="memberNameList">
        <!-- list标签：准备一组集合类型的数据，给集合属性赋值 -->
        <!--<list>
            <value>member01</value>
            <value>member02</value>
            <value>member03</value>
        </list>-->
        <!-- 使用set标签也能实现相同效果，只是附带了去重功能 -->
        <!--<set>
            <value>member01</value>
            <value>member02</value>
            <value>member02</value>
        </set>-->
        <!-- array也同样兼容 -->
        <array>
            <value>member01</value>
            <value>member02</value>
            <value>member02</value>
        </array>
    </property>
    <property name="managerList">
        <!-- 给Map类型的属性赋值 -->
        <!--<map>
            <entry key="财务部" value="张三"/>
            <entry key="行政部" value="李四"/>
            <entry key="销售部" value="王五"/>
        </map>-->
        <!-- 也可以使用props标签 -->
        <props>
            <prop key="财务部">张三2</prop>
            <prop key="行政部">李四2</prop>
            <prop key="销售部">王五2</prop>
        </props>
    </property>
</bean>
```





[上一个实验](experiment12.html) [回目录](../verse03.html) [下一个实验](experiment14.html)
