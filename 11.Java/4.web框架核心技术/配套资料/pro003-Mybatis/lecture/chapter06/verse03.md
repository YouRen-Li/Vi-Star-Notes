[[toc]]

# 第三节 QBC查询

## 1、概念

QBC：Query By Criteria

![./images](./images/img010.png)

QBC查询最大的特点就是将SQL语句中的WHERE子句进行了组件化的封装，让我们可以通过调用Criteria对象的方法自由的拼装查询条件。



## 2、例子

```java
        // 1.创建EmployeeExample对象
        EmployeeExample example = new EmployeeExample();

        // 2.通过example对象创建Criteria对象
        EmployeeExample.Criteria criteria01 = example.createCriteria();
        EmployeeExample.Criteria criteria02 = example.or();

        // 3.在Criteria对象中封装查询条件
        criteria01
            .andEmpAgeBetween(9, 99)
            .andEmpNameLike("%o%")
            .andEmpGenderEqualTo("male")
            .andEmpSalaryGreaterThan(500.55);

        criteria02
                .andEmpAgeBetween(9, 99)
                .andEmpNameLike("%o%")
                .andEmpGenderEqualTo("male")
                .andEmpSalaryGreaterThan(500.55);

        SqlSession session = factory.openSession();

        EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

        // 4.基于Criteria对象进行查询
        List<Employee> employeeList = mapper.selectByExample(example);

        for (Employee employee : employeeList) {
            System.out.println("employee = " + employee);
        }

        session.close();

        // 最终SQL的效果：
        // WHERE ( emp_age between ? and ? and emp_name like ? and emp_gender = ? and emp_salary > ? ) or( emp_age between ? and ? and emp_name like ? and emp_gender = ? and emp_salary > ? )
```



[上一节](verse02.html) [回目录](index.html)