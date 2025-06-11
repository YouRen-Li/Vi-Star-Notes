[[toc]]

# 第七节 sql标签



## 1、抽取重复的SQL片段

```xml
    <!-- 使用sql标签抽取重复出现的SQL片段 -->
    <sql id="mySelectSql">
        select emp_id,emp_name,emp_age,emp_salary,emp_gender from t_emp
    </sql>
```



## 2、引用已抽取的SQL片段

```xml
        <!-- 使用include标签引用声明的SQL片段 -->
        <include refid="mySelectSql"/>
```



[上一节](verse06.html) [回目录](index.html)