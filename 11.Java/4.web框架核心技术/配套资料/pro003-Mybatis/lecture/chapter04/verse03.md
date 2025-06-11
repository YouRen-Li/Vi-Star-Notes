[[toc]]

# 第三节 set标签



## 1、相关业务需求举例

实际开发时，对一个实体类对象进行更新。往往不是更新所有字段，而是更新一部分字段。此时页面上的表单往往不会给不修改的字段提供表单项。

```html
<form action="" method="">
    
	<input type="hidden" name="userId" value="5232" />
	
	年  龄：<input type="text" name="userAge" /><br/>
	性  别：<input type="text" name="userGender" /><br/>
	坐  标：<input type="text" name="userPosition" /><br/>
	<!-- 用户名：<input type="text" name="userName" /><br/>   -->
	<!-- 余  额：<input type="text" name="userBalance" /><br/>-->
	<!-- 等  级：<input type="text" name="userGrade" /><br/>  -->
	
	<button type="submit">修改</button>
	
</form>
```

例如上面的表单，如果服务器端接收表单时，使用的是User这个实体类，那么userName、userBalance、userGrade接收到的数据就是null。<br/>

如果不加判断，直接用User对象去更新数据库，在Mapper配置文件中又是每一个字段都更新，那就会把userName、userBalance、userGrade设置为null值，从而造成数据库表中对应数据被破坏。<br/>

此时需要我们在Mapper配置文件中，对update语句的set子句进行定制，此时就可以使用动态SQL的set标签。



## 2、实际配置方式

```xml
<!-- void updateEmployeeDynamic(Employee employee) -->
<update id="updateEmployeeDynamic">
    update t_emp
    <!-- set emp_name=#{empName},emp_salary=#{empSalary} -->
    <!-- 使用set标签动态管理set子句，并且动态去掉两端多余的逗号 -->
    <set>
        <if test="empName != null">
            emp_name=#{empName},
        </if>
        <if test="empSalary &lt; 3000">
            emp_salary=#{empSalary},
        </if>
    </set>
    where emp_id=#{empId}
    <!--
         第一种情况：所有条件都满足 SET emp_name=?, emp_salary=?
         第二种情况：部分条件满足 SET emp_salary=?
         第三种情况：所有条件都不满足 update t_emp where emp_id=?
            没有set子句的update语句会导致SQL语法错误
     -->
</update>
```



[上一节](verse02.html) [回目录](index.html) [下一节](verse04.html)

