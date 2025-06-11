# 第4章-商品SKU管理

**学习目标：**

-   熟悉商品SKU相关的数据模型
-   完成**保存商品SKU功能**
-   完成商品SKU列表功能
-   完成商品SKU上下架功能
-   了解商品详情业务
-   模板技术Thymeleaf入门

# 1. 业务介绍

## 1.1 数据库表结构

根据以上的需求，以此将SKU关联的数据库表结构设计为如下：

![](image/wps12_gBdwKsAj5z.jpg)

## 1.2 数据准备

### 1.2.1 平台属性添加

![](image/wps13_QwjCQjBpQa.jpg)

### 1.2.2 商品spu管理

![](image/wps14_R0QHMiWb18.jpg)

添加销售属性信息

![](image/wps15_axvmle_abK.jpg)

# 2. 保存skuInfo功能

## 2.1 生成基础代码

-   sku\_info： 商品sku信息表
-   sku\_sale\_attr\_value：sku销售属性值关联表
-   sku\_attr\_value：sku平台属性值关联表
-   sku\_image：sku商品图片表

## 2.2 销售属性

![](image/image-20221213011411802_qUuNRNrBTx.png)

### 2.2.1. 编写控制器

在SpuManageController处理查询SPU销售属性方法

```java
/**
     * 根据spuId 获取到销售属性集合
     * @param spuId
     * @return
     */
@GetMapping("/spuSaleAttrList/{spuId}")
public Result getSpuSaleAttrList(@PathVariable Long spuId){
  //  调用服务层方法 集合的泛型
  List<SpuSaleAttr> spuSaleAttrList = this.manageService.getSpuSaleAttrList(spuId);
  return Result.ok(spuSaleAttrList);
}
```

### 2.2.2. 业务接口以及实现类

ManageService中增加接口

```java
/**
 * 根据SPUID查询商品销售属性列表(包含属性值)
 *
 * @param spuId
 * @return
 */
List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);
```

ManageServiceImpl实现类中实现该方法

```java
/**
 * 根据SPUID查询商品销售属性列表(包含属性值)
 * @param spuId
 * @return
 */
@Override
public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
  //  调用mapper 层
  return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
}
```

### 2.2.3. 持久层

SpuSaleAttrMapper中增加自定义接口

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {
    /**
     * 根据spuId 查询销售属性集合
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(Long spuId);
}
```

对应的在resource/mapper目录下创建映射文件SpuSaleAttrMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<!--非必要属性：namespace  接口全路径-->
<mapper namespace="com.atguigu.gmall.product.mapper.SpuSaleAttrMapper">
    <!--配置关系映射-->
    <resultMap id="SpuSaleAttrMap" type="com.atguigu.gmall.model.product.SpuSaleAttr" autoMapping="true">
        <id property="id" column="id"></id>
        <collection property="spuSaleAttrValueList" ofType="com.atguigu.gmall.model.product.SpuSaleAttrValue" autoMapping="true">
            <id property="id" column="spu_slae_attr_id"></id>
        </collection>
    </resultMap>
    <!--
    sql 片段 : 复用字段
    -->
    <sql id="spuSql">
        ssa.id,
            ssa.spu_id,
            ssa.base_sale_attr_id,
            ssa.sale_attr_name,
            ssav.id spu_slae_attr_id,
            ssav.sale_attr_value_name
    </sql>

    <!--执行sql 语句-->
    <select id="selectSpuSaleAttrList" resultMap="SpuSaleAttrMap">
        select
            <include refid="spuSql"></include>
        from spu_sale_attr ssa inner join spu_sale_attr_value ssav
                                          on ssa.spu_id = ssav.spu_id and ssa.base_sale_attr_id = ssav.base_sale_attr_id
        where ssav.spu_id = #{spuId}
        order by ssa.id
    </select>
</mapper>
```

## 2.3 图片加载功能

![](image/image-20221213011431420_eB1soK0tHb.png)

功能分析：图片列表是根据spuId得来，涉及到的数据库表spu\_image

### 2.3.1. 添加控制器

在`service-product`模块中Spu控制器类：SpuManageController

```java
/**
 * 根据商品SpuID查询当前商品所有图片
 * @param spuId
 * @return
 */
@GetMapping("/spuImageList/{spuId}")
public Result getSpuImageList(@PathVariable("spuId") Long spuId){
    List<SpuImage> list = spuManageService.getSpuImageList(spuId);
    return Result.ok(list);
}
```

### 2.3.2. 业务接口与实现类

在`SpuManageService`接口中新增方法

```java
/**
 * 根据商品SpuID查询当前商品所有图片
 * @param spuId
 * @return
 */
List<SpuImage> getSpuImageList(Long spuId);
```

在`SpuManageServiceImpl`中实现上面方法

```java
/**
 * 根据商品SpuID查询当前商品所有图片
 *
 * @param spuId
 * @return
 */
@Override
public List<SpuImage> getSpuImageList(Long spuId) {
        List<SpuImage> spuImageList = spuImageService.list(new LambdaQueryWrapper<SpuImage>().eq(SpuImage::getSpuId, spuId).orderByDesc(SpuImage::getId));
}
```

## 2.4 保存SKU

### 2.4.1. 编写控制器

新建SkuManageController中提供处理保存SKU方法

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.model.SkuInfo;
import com.atguigu.gmall.product.service.SkuManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: atguigu
 * @create: 2023-02-24 11:39
 */
@RestController
@RequestMapping("/admin/product")
public class SkuManageController {

    @Autowired
    private SkuManageService skuManageService;


    /**
     * 保存商品SKU信息
     * @param skuInfo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuManageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
}
```

### 2.4.2. 业务接口与实现

创建业务接口：SkuManageService接口中增加方法

```
public interface SkuInfoService extends IService<SkuInfo> {

}
public interface SkuImageService extends IService<SkuImage> {

}

public interface SkuAttrValueService extends IService<SkuAttrValue> {

}

public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

}

@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService {

}
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
    implements SkuImageService {

}

@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService {

}

@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue>
    implements SkuSaleAttrValueService {

}

```



```java
package com.atguigu.gmall.product.service;


import com.atguigu.gmall.product.model.SkuInfo;

public interface SkuManageService {
    /**
     * 保存商品SKU信息
     * @param skuInfo
     * @return
     */
    void saveSkuInfo(SkuInfo skuInfo);
}
```

创建业务实现类：SkuManageServiceImpl实现类中实现上面方法

```java
package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.model.SkuAttrValue;
import com.atguigu.gmall.product.model.SkuImage;
import com.atguigu.gmall.product.model.SkuInfo;
import com.atguigu.gmall.product.model.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: atguigu
 * @create: 2023-02-24 11:40
 */
@Service
public class SkuManageServiceImpl implements SkuManageService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;


    /**
     * 保存商品SKU信息
     * 1.将前端提交商品SKU基本信息存入到sku_info表
     * 2.将前端提交当前SKU相关图片集合 存入到 sku_image表
     * 3.将前端提交当前SKU的平台属性信息 存入到 sku_attr_value表
     * 4.将前端提交当前SKU的销售属性信息 存入到 sku_sale_attr_value 表
     *
     * @param skuInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuInfo(SkuInfo skuInfo) {
        //1.将前端提交商品SKU基本信息存入到sku_info表
        skuInfoService.save(skuInfo);
        //2.将前端提交当前SKU相关图片集合 存入到 sku_image表
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (!CollectionUtils.isEmpty(skuImageList)) {
            skuImageList.stream().forEach(skuImage -> {
                //2.1 将商品SKU图片关联到SKU
                skuImage.setSkuId(skuInfo.getId());
            });
            //2.2 批量保存商品SKU图片
            skuImageService.saveBatch(skuImageList);
        }
        //3.将前端提交当前SKU的平台属性信息 存入到 sku_attr_value表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            //2.1 将平台属性信息关联到SKU
            skuAttrValueList.stream().forEach(skuAttrValue -> {
                skuAttrValue.setSkuId(skuInfo.getId());
            });
            //2.2 批量保存平台属性
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
        //4.将前端提交当前SKU的销售属性信息 存入到 sku_sale_attr_value 表
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            skuSaleAttrValueList.stream().forEach(skuSaleAttrValue -> {
                //4.1 销售属性关联SPU
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                //4.2 销售属性关联SKU
                skuSaleAttrValue.setSkuId(skuInfo.getId());
            });
            //4.3 批量保存SKU销售属性信息
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
        }
    }
}
```

# 3. SKU列表及上下架处理

## 3.1 SKU列表

### 3.1.1 编写控制器

SkuManageController 控制器

```java
    /**
     * 根据分类ID分页查询商品SKU列表
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result getSkuListByPage(@PathVariable("page") Long page, @PathVariable("limit") Long limit, @RequestParam("category3Id") Long category3Id) {
        //1.构建分页对象
        IPage<SkuInfo> iPage = new Page<>(page, limit);
        //2.查询分页数据 alt+enter 快速修正错误
        iPage = skuManageService.getSkuListByPage(iPage, category3Id);
        return Result.ok(iPage);
    }
```

### 3.1.2 业务接口与实现类

在SkuManageService 接口中添加方法

```java
/**
 * 根据分类ID分页查询商品SKU列表
 * @param iPage
 * @param category3Id
 * @return
 */
IPage<SkuInfo> getSkuListByPage(IPage<SkuInfo> iPage, Long category3Id);
```

在SkuManageServiceImpl 实现类中实现上面方法

```java
/**
 * 根据分类ID分页查询商品SKU列表
 *
 * @param iPage
 * @param category3Id
 * @return
 */
@Override
public IPage<SkuInfo> getSkuListByPage(IPage<SkuInfo> iPage, Long category3Id) {
    LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
    if (category3Id != null) {
        queryWrapper.eq(SkuInfo::getCategory3Id, category3Id);
    }
    queryWrapper.orderByDesc(SkuInfo::getUpdateTime);
    return skuInfoService.page(iPage, queryWrapper);
}
```

## 3.2  上下架处理

### 3.2.1 编写控制器

SkuManageController 控制器

```java
/**
 * 商品SKU上架
 * @param skuId
 * @return
 */
@GetMapping("/onSale/{skuId}")
public Result onSale(@PathVariable("skuId") Long skuId){
    skuManageService.onSale(skuId);
    return Result.ok();
}


/**
 * 商品SKU下架
 * @param skuId
 * @return
 */
@GetMapping("/cancelSale/{skuId}")
public Result cancelSale(@PathVariable("skuId") Long skuId){
    skuManageService.cancelSale(skuId);
    return Result.ok();
}
```

### 3.2.2 业务接口与实现类

在SkuManageService 接口中新增方法

```java
/**
 * 商品上架
 * @param skuId
 */
void onSale(Long skuId);

/**
 * 商品下架
 * @param skuId
 */
void cancelSale(Long skuId);
```

在SkuManageServiceImpl 实现类中实现上面方法

```java
/**
 * 商品SKU上架
 *
 * @param skuId
 * @return
 */
@Override
public void onSale(Long skuId) {
    //1.修改数据库中上架状态
    SkuInfo skuInfo = new SkuInfo();
    skuInfo.setId(skuId);
    skuInfo.setIsSale(1);
    skuInfoService.updateById(skuInfo);
    //2.TODO 将来还需要同步将索引库ES的商品进行上架;需要构建商品缓存到Redis
}

/**
 * 商品SKU下架
 *
 * @param skuId
 * @return
 */
@Override
public void cancelSale(Long skuId) {
    //SkuInfo skuInfo = skuInfoService.getById(skuId);
    //if (skuInfo != null && skuInfo.getIsSale() != 0) {
    //    skuInfo.setIsSale(0);
    //    skuInfoService.updateById(skuInfo);
    //}
    LambdaUpdateWrapper<SkuInfo> updateWrapper = new LambdaUpdateWrapper<>();
    //1.设置更新条件
    updateWrapper.eq(SkuInfo::getId, skuId);
    //1.设置更新字段值
    updateWrapper.set(SkuInfo::getIsSale, 0);
    skuInfoService.update(updateWrapper);

    //2.TODO 将来还需要同步将索引库ES的商品进行下架;需要删除商品缓存Redis
}
```

# 4. 商品详情相关业务介绍

-   商品详情页，简单说就是以购物者的角度展现一个sku的详情信息。
-   用户点击不同的销售属性值切换不同的商品
-   点击添加购物车，将商品放入购物车列表中

![](image/wps1_zK-ULyf6nX.png)

# 5. 模板技术Thymeleaf介绍

## 5.1 Thymeleaf 简介

​	Thymeleaf是一款用于渲染XML/XHTML/HTML5内容的模板引擎。它也可以轻易的与Spring MVC等Web框架进行集成作为Web应用的模板引擎。与其它模板引擎相比， Thymeleaf最大的特点是能够直接在浏览器中打开并正确显示模板页面，而不需要启动整个Web应用！

类似模板技术

-   freemarker
-   [Velocity](http://velocity.apache.org/ "Velocity")

官方网站：[https://www.thymeleaf.org/index.html](https://www.thymeleaf.org/index.html "https://www.thymeleaf.org/index.html")

## 5.2 快速入门

1.  新建一个demo模块：thymeleaf-demo，依赖模块web，Thymeleaf.模板。

![](image/image-20221213104301629_P382Oc8m91.png)

1.  pom.xml
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <groupId>com.atguigu</groupId>
        <artifactId>thymeleaf-demo</artifactId>
        <version>1.0-SNAPSHOT</version>

        <!--引入spring boot 父工程-->
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.3.6.RELEASE</version>
        </parent>
    ```


        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-thymeleaf</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
    
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </project>
    ​```
2.  启动类
    ```java
    package com.atguigu.demo;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    /**
     * @author: atguigu
     * @create: 2022-12-13 10:45
     */
    @SpringBootApplication
    public class ThymeleafDemoApp {

        public static void main(String[] args) {
            SpringApplication.run(ThymeleafDemoApp.class, args);
        }
    }
    ```

3.  创建application.yml
    ```yaml
    server:
      port: 9999
    spring:
      thymeleaf:
        #关闭Thymeleaf的缓存
        cache: false
    ```

4.  不需要做任何配置，启动器已经帮我们把Thymeleaf的视图器配置完成：

    ![](image/1526435647041_UmFPNX0fbg.png)





    而且，还配置了模板文件（html）的位置，与jsp类似的前缀+ 视图名 + 后缀风格：
![](image/1526435706301_dNzO8eN03n.png)


    -   默认前缀：`classpath:/templates/`
    -   默认后缀：`.html`
    所以如果我们返回视图：`users`，会指向到 `classpath:/templates/users.html`
5.  准备一个controller，控制视图跳转。**注意：控制器上要使用@Controller注解**
    ```java
    package com.atguigu.demo.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;

    /**
     * @author: atguigu
     * @create: 2022-12-13 10:56
     */
    @Controller
    public class HelloController {

        @GetMapping("/hello")
        public String show1(Model model){
            model.addAttribute("msg", "Hello, Thymeleaf!");
            return "hello";
        }
    }
    ```

6.  在resources目录下新建文件夹：templates

    ![](image/image-20221213105846344_mhBx2gQWd7.png)




7.  在`templates`目录下新建一个html模板文件：hello.html
    ```html
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>hello</title>
    </head>
    <body>
        <h1 th:text="${msg}">你好</h1>
    </body>
    </html>
    ```

8.  启动项目，访问页面：

    ![](image/image-20221213110053156_MZxpR6Ontu.png)



### 5.2.1 设置头文件

就像JSP的<%@Page %>一样 ，Thymeleaf的也要引入标签规范。不加这个虽然不影响程序运行，但是你的idea会不识别标签，不方便开发。

```html
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
```

### 5.2.2 赋值字符串拼接

> request.setAttribute("name", "刘德华");

```html
<p th:text="'hello' + ${name}"></p>
```

### 5.2.3 循环

> List\<String> list = Arrays.asList("郑爽", "刘德华", "张惠妹", "成龙");
> request.setAttribute("list", list);

```html
<table>
  <!--s 表示集合中的元素 ${list}表示后台存储的集合 -->
    <tr th:each="s,stat: ${list}">
        <td th:text="${s}"></td>
        <td th:text="${stat.index}"></td>
        <td th:text="${stat.count}"></td>
        <td th:text="${stat.size}"></td>
        <td th:text="${stat.even}"></td>
        <td th:text="${stat.odd}"></td>
        <td th:text="${stat.first}"></td>
        <td th:text="${stat.last}"></td>
    </tr>
</table>
```

| 语法关键字    | 解释                      |
| -------- | ----------------------- |
| stat     | 称作状态变量                  |
| index    | 当前迭代对象的 index（从 0 开始计算） |
| count    | 当前迭代对象的 index（从 1 开始计算） |
| size     | 被迭代对象的大小                |
| even/odd | 布尔值，当前循环是否是偶数/奇数        |
| first    | 布尔值，当前循环是否是第一个          |
| last     | 布尔值，当前循环是否是最后一个         |

### 5.2.4 判断

th:if 条件成立显示

th:unless 条件不成立的时候才会显示内容

> model.addAttribute("age",18);

```html
<h2>判断 if</h2>
<div th:if="${age}>=18" th:text="success">good</div>
<a th:unless="${age != 18}" th:text="success" >atguigu</a>
<h2>判断 三元</h2>
<div th:text="${age}>=18?'success':'failure'"></div>
```

### 5.2.5 取session中的属性

> httpSession.setAttribute("addr","北京中南海");

```html
<div th:text="${session.addr}"> </div>
```

### 5.2.6 引用内嵌页

top.html内容如下

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>hello</title>
</head>
<body>
<html>
我是公共的内容组件
</html>
```

在hello.html中引入

```html
<div th:include="top"/>
```

### 5.2.7 th:utext :解析样式

th:utext:识别html中的标签

> request.setAttribute("gname","\<span style=color:green>绿色\</span>");

```html
<p th:utext="${gname}">color</p>
```

### 5.2.8 点击链接传值

```java
@RequestMapping("list.html")
public String list(String category1Id, HttpServletRequest request){
 // 接收传递过来的数据
    System.out.println("获取到的数据：\t"+category1Id);
    /*保存 category1Id*/
    request.setAttribute("category1Id",category1Id);
    return category1Id;
   }
```

```html
<a th:href="@{http://localhost:9999/list.html?category1Id={category1Id}(category1Id=${category1Id})}">点我带你飞</a>
```

### 5.2.9 多种存储方式

```java
HashMap<String, Object> map = new HashMap<>();
map.put("stuNo","1000");
map.put("stuName","张三");
model.addAllAttributes(map);
```

```html
<h2> 多种方式存储数据</h2>

<div th:text="${stuNo}"></div>
<div th:text="${stuName}"></div>
```

### 5.2.10 字符串替换

```纯文本
request.setAttribute("today", new Date());
```

strings 操作字符串的工具类，还有 @dates ，#numbers 等工具类。

```纯文本
<p th:text="${#dates.format(today,'yyyy-MM-dd HH:mm:ss')}"></p>
<p th:text="${#numbers.formatCurrency('9999')}"></p>  ￥1,000.00
```

替换：
后台代码：
request.setAttribute("str","atguigu");

页面：
[http://localhost:8080/atguigu](http://localhost:8080/atguigu "http://localhost:8080/atguigu")

```纯文本
<p th:text="${#strings.concat(str, 'xxxxxx')}">aaa</p>

<p th:text="${#strings.replace(str,'a','i')}">bbb</p>
```
