[[toc]]

# 第一节 实体类类型别名

## 1、目标

让Mapper配置文件中使用的实体类类型名称更简洁。



## 2、操作

### ①Mybatis全局配置文件

```xml
<!-- 配置类型的别名 -->
<typeAliases>
    <!-- 声明了实体类所在的包之后，在Mapper配置文件中，只需要指定这个包下的简单类名即可 -->
    <package name="com.atguigu.mybatis.entity"/>
</typeAliases>
```



### ②Mapper配置文件

```xml
<!-- Employee selectEmployeeById(Integer empId); -->
<select id="selectEmployeeById" resultType="Employee">
    select emp_id,emp_name,emp_salary,emp_gender,emp_age from t_emp
    where emp_id=#{empId}
</select>
```



## 3、Mybatis内置的类型别名

![./images](./images/img001.png)



[回目录](index.html) [下一节](verse02.html)