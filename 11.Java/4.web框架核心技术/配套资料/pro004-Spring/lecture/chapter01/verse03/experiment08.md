[[toc]]

# 实验八 给bean的属性赋值：构造器注入

## 1、声明组件类

```java
package com.atguigu.ioc.component;
    
public class HappyTeam {
        
    private String teamName;
    private Integer memberCount;
    private Double memberSalary;
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public Integer getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
    
    public Double getMemberSalary() {
        return memberSalary;
    }
    
    public void setMemberSalary(Double memberSalary) {
        this.memberSalary = memberSalary;
    }
    
    @Override
    public String toString() {
        return "HappyTeam{" +
                "teamName='" + teamName + '\'' +
                ", memberCount=" + memberCount +
                ", memberSalary=" + memberSalary +
                '}';
    }
    
    public HappyTeam(String teamName, Integer memberCount, Double memberSalary) {
        this.teamName = teamName;
        this.memberCount = memberCount;
        this.memberSalary = memberSalary;
    }
    
    public HappyTeam() {
    }
}
```





## 2、配置

```xml
<!-- 实验八 给bean的属性赋值：构造器注入 -->
<bean id="happyTeam" class="com.atguigu.ioc.component.HappyTeam">
    <constructor-arg value="happyCorps"/>
    <constructor-arg value="10"/>
    <constructor-arg value="1000.55"/>
</bean>
```



## 3、测试

```java
@Test
public void testExperiment08() {
    
    HappyTeam happyTeam = iocContainer.getBean(HappyTeam.class);
    
    System.out.println("happyTeam = " + happyTeam);
    
}
```



## 4、补充

constructor-arg标签还有两个属性可以进一步描述构造器参数：

- index属性：指定参数所在位置的索引（从0开始）
- name属性：指定参数名



[上一个实验](experiment07.html) [回目录](../verse03.html) [下一个实验](experiment09.html)