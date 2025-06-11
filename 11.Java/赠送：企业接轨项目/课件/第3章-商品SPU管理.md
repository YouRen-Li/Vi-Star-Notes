# 第3章-商品SPU管理

**学习目标：**

-   完成品牌管理
-   了解SPU商品相关的概念
-   完成SPU列表功能
-   掌握MinIO分布式文件存储服务
-   完成商品SPU保存功能

# 1. 品牌管理

相关表结构：



![](image/image-20221212234420609_zTWnt2WIaM.png)

## 1.1 生成基础代码

使用mybatisx生成品牌相关的表到`service-product`商品微服务模块，相关表名称如下

-   base\_trademark 品牌表
-   base\_category\_trademark 分类品牌中间表

## 1.2 品牌分页查询

需求：

![](image/image-20221212204805066_8VgDFIJAWz.png)

> 

### 1.2.1 控制器BaseTrademarkController

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.model.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.atguigu.gmall.product.service.BaseTrademarkService;

import org.springframework.web.bind.annotation.RestController;

/**
 * 品牌表 前端控制器
 *
 * @author atguigu
 * @since 2022-12-24
 */
@Api(tags = "品牌表控制器")
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;


      /**
     * 品牌分页查询
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result getBaseTrademarkByPage(@PathVariable("page") Long page, @PathVariable("limit") Long limit){
        //封装MP分页对象-封装页码 页大小
        IPage<BaseTrademark> iPage = new Page<>(page, limit);
        //调用业务逻辑进行分页查询封装其他数据
        iPage = baseTrademarkService.getBaseTrademarkByPage(iPage);
        return Result.ok(iPage);
    }


}
```

### 1.2.2 业务接口BaseTrademarkService

```java
package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.model.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 品牌表 业务接口类
 * @author atguigu
 * @since 2022-12-24
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {

    /**
     * 品牌分页查询
     * @param iPage
     * @return
     */
    IPage<BaseTrademark> getBaseTrademarkByPage(IPage<BaseTrademark> iPage);
}
```

### 1.2.3 业务实现类BaseTrademarkServiceImpl

```java
package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.base.model.BaseEntity;
import com.atguigu.gmall.product.model.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 品牌表 业务实现类
 *
 * @author atguigu
 * @since 2022-12-24
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark> implements BaseTrademarkService {

    /**
     * 品牌分页查询
     *
     * @param iPage
     * @return
     */
    @Override
    public IPage<BaseTrademark> getBaseTrademarkByPage(IPage<BaseTrademark> iPage) {
        //分页条件
        LambdaQueryWrapper<BaseTrademark> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(BaseTrademark::getUpdateTime);
        //调用业务层或者持久层对象分页方法
        return this.page(iPage, queryWrapper);
    }
}
```

### 1.2.4 持久层mapper

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.BaseTrademark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BaseTrademarkMapper extends BaseMapper<BaseTrademark> {

}
```

### 1.2.5 其他业务代码

BaseTrademarkController 中完成品牌的增删改查，调用公共业务接口方法即可。

```java
package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.model.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.atguigu.gmall.product.service.BaseTrademarkService;

import org.springframework.web.bind.annotation.RestController;

/**
 * 品牌表 前端控制器
 *
 * @author atguigu
 * @since 2022-12-24
 */
@Api(tags = "品牌表控制器")
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;


    /**
     * 品牌保存
     * @param baseTrademark
     * @return
     */
    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }


    /**
     * 根据品牌ID查询品牌信息
     * @param id
     * @return
     */
    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademarkById(@PathVariable("id") Long id){
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /**
     * 更新品牌
     * @param baseTrademark
     * @return
     */
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result deleteBaseTrademarkById(@PathVariable("id") Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}
```

## 2.2 分类品牌管理

### 1.2.1 控制器

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.service.BaseCategoryTradeMarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
public class BaseCategoryTradeMarkController {

    @Autowired
    private BaseCategoryTradeMarkService baseCategoryTradeMarkService;

    /**
     * 根据三级分类Id 查询品牌列表
     * @param category3Id
     * @return
     */
    @GetMapping("/findTrademarkList/{category3Id}")
    public Result findTrademarkList(@PathVariable Long category3Id){
        //  服务层方法.
        List<BaseTrademark>  baseTrademarkList = this.baseCategoryTradeMarkService.getTrademarkList(category3Id);
        //  返回数据
        return Result.ok(baseTrademarkList);
    }

    /**
     * 根据三级分类Id 获取到可选品牌列表。
     * @param category3Id
     * @return
     */
    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    public Result findCurrentTrademarkList (@PathVariable Long category3Id){
        //  服务层方法.
        List<BaseTrademark>  baseTrademarkList = this.baseCategoryTradeMarkService.getCurrentTrademarkList(category3Id);
        //  返回数据
        return Result.ok(baseTrademarkList);
    }

    /**
     * 保存分类Id 与 品牌的关系
     * @param categoryTrademarkVo
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        //  调用服务层方法.
        this.baseCategoryTradeMarkService.save(categoryTrademarkVo);
        return Result.ok();
    }

    /**
     * 删除品牌与分类的关系
     * @param category3Id
     * @param trademarkId
     * @return
     */
    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    public Result remove(@PathVariable Long category3Id,
                         @PathVariable Long trademarkId) {
        //  调用服务层方法.
        //  属于逻辑删除。
        LambdaQueryWrapper<BaseCategoryTrademark> trademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id,category3Id);
        trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getTrademarkId,trademarkId);
        this.baseCategoryTradeMarkService.remove(trademarkLambdaQueryWrapper);
        //  使用了方法重载删除数据.
        //  this.baseCategoryTradeMarkService.remove(category3Id,trademarkId);
        return Result.ok();
    }
}
```

### 3.2.2 业务接口

```java
package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryTradeMarkService extends IService<BaseCategoryTrademark> {
    /**
     * 根据三级分类Id 查询品牌列表
     * @param category3Id
     * @return
     */
    List<BaseTrademark> getTrademarkList(Long category3Id);

    /**
     * 根据三级分类Id 获取到可选品牌列表。
     * @param category3Id
     * @return
     */
    List<BaseTrademark> getCurrentTrademarkList(Long category3Id);

    /**
     * 保存分类Id 与品牌对应关系数据
     * @param categoryTrademarkVo
     */
    void save(CategoryTrademarkVo categoryTrademarkVo);

    /**
     * 删除品牌与分类的关系
     * @param category3Id
     * @param trademarkId
     */
    void remove(Long category3Id, Long trademarkId);

}

```

### 3.2.3 业务实现类

```java
package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseCategoryTradeMarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseCategoryTradeMarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper,BaseCategoryTrademark> implements BaseCategoryTradeMarkService {

    @Autowired
    private BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;
    @Override
    public List<BaseTrademark> getTrademarkList(Long category3Id) {
        try {
            //  根据三级分类Id 查询品牌Id
            LambdaQueryWrapper<BaseCategoryTrademark> trademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
            trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id,category3Id);
            List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(trademarkLambdaQueryWrapper);

            //  判断
            if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)){
                //  这个集合中包含tmId;
                //            ArrayList<Long> tmIdList = new ArrayList<>();
                //            baseCategoryTrademarkList.forEach(baseCategoryTrademark -> {
                //                tmIdList.add(baseCategoryTrademark.getTrademarkId());
                //            });
                //  使用map 集合映射   R apply(T t);
                //            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> {
                //                return baseCategoryTrademark.getTrademarkId();
                //            }).collect(Collectors.toList());
                List<Long> tmIdList = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> baseCategoryTrademark.getTrademarkId()).collect(Collectors.toList());
                //  查询品牌集合列表。
                List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectBatchIds(tmIdList);
                //  返回数据
                return baseTrademarkList;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //  返回null
        return null;
    }

    @Override
    public List<BaseTrademark> getCurrentTrademarkList(Long category3Id) {
        //  根据三级分类Id 查询品牌Id
        //  select * from base_category_trademark where category3_id = 61 and is_deleted = 0;
        LambdaQueryWrapper<BaseCategoryTrademark> trademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id,category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(trademarkLambdaQueryWrapper);
        //  判断 这个分类Id 与 品牌 是否进行绑定！
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)){
            //  获取以绑定的品牌Id
            //  1,2,3
            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> baseCategoryTrademark.getTrademarkId()).collect(Collectors.toList());
            //  查询品牌集合列表。
            /*
             baseTrademarkList select * from base_trademark where id in (1,2,3);
            1,小米,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpLiAP5NOAAAFtscZn_s397.png
            2,苹果,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpOuAXIroAAA8KOpezoQ651.png
            3,华为,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpUuAVioLAAGXnmYhX7M923.jpg
             */
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectBatchIds(tmIdList);

             /*
            查询所有品牌列表：baseTrademarkMapper.selectList(null) 1,2,3,5,6
            1,小米,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpLiAP5NOAAAFtscZn_s397.png
            2,苹果,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpOuAXIroAAA8KOpezoQ651.png
            3,华为,http://47.93.148.192:8080/group1/M00/01/71/rBHu8mEQpUuAVioLAAGXnmYhX7M923.jpg
            5,AA,http://47.93.148.192:9000/gmall/16312563680039569579c-dc7b-438d-9012-7f6a69532a66
            6,TT,http://192.168.200.129:9000/gmall/1631257139730ec6718d7-2ed2-46e5-8f5a-9af4b1e7a505
             */
            //  boolean test(T t);
            List<BaseTrademark> trademarkList = baseTrademarkMapper.selectList(null).stream().filter(baseTrademark -> {
                return !tmIdList.contains(baseTrademark.getId());
            }).collect(Collectors.toList());
            //  返回数据
            return trademarkList;
        }
        //  三级分类Id 与品牌没有绑定的时候;
        return baseTrademarkMapper.selectList(null);
    }

    @Override
    public void save(CategoryTrademarkVo categoryTrademarkVo) {
        //  先获取到品牌Id 列表; baseCategoryTrademark (61,5) (61,6)
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();

        //  声明一个集合来存储品牌与分类关系！
        //        ArrayList<BaseCategoryTrademark> baseCategoryTrademarkArrayList = new ArrayList<>();
        //        if(!CollectionUtils.isEmpty(trademarkIdList)){
        //            trademarkIdList.forEach(tmId->{
        //                //  将数据保存到 base_category_trademark 表;
        //                BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
        //                baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
        //                baseCategoryTrademark.setTrademarkId(tmId);
        //                //  循环执行insert 语句;
        //                //  baseCategoryTrademarkMapper.insert(baseCategoryTrademark);
        //                baseCategoryTrademarkArrayList.add(baseCategoryTrademark);
        //            });
        //        }
        //  map 做关系映射：  R apply(T t);  stream(): --- 对集合或数组 中的元素 进行运算， 但是不会改变原有数据集. 保存运算之后的结果集，
        //  应该使用collect(Collectors.toList())
        List<BaseCategoryTrademark> baseCategoryTrademarkArrayList = trademarkIdList.stream().map(tmId -> {
            //  声明对象保存数据
            BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
            baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
            baseCategoryTrademark.setTrademarkId(tmId);
            //  返回数据.
            return baseCategoryTrademark;
        }).collect(Collectors.toList());
        //  批量插入数据：base_category_trademark 让这个接口继承 IService
        //  this: 表示当前这个实现类的对象;
        this.saveBatch(baseCategoryTrademarkArrayList);
    }

    @Override
    public void remove(Long category3Id, Long trademarkId) {
        //  属于逻辑删除。
        LambdaQueryWrapper<BaseCategoryTrademark> trademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id,category3Id);
        trademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getTrademarkId,trademarkId);
        baseCategoryTrademarkMapper.delete(trademarkLambdaQueryWrapper);
    }
}
```

### 3.2.4  查询可选匹配列表方式二：Mapper

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.BaseTrademark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌表 Mapper 接口
 *
 * @author atguigu
 * @since 2023-02-22
 */
public interface BaseTrademarkMapper extends BaseMapper<BaseTrademark> {

    List<BaseTrademark> getCurrentTrademarkList(@Param("category3Id") Long category3Id);
}
```

**自定义SQL**

BaseTrademarkMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.gmall.product.mapper.BaseTrademarkMapper">


    <!--查询指定分类未关联的品牌列表-->
    <select id="getCurrentTrademarkList" resultType="com.atguigu.gmall.product.model.BaseTrademark">
        SELECT
            a.*
        FROM
            ( SELECT * FROM base_trademark bt WHERE bt.is_deleted = '0' ) a
            LEFT JOIN ( SELECT * FROM base_category_trademark bct WHERE bct.is_deleted = '0' AND bct.category3_id = #{category3Id} ) b ON a.id = b.trademark_id
        WHERE
            b.id IS NULL
    </select>
</mapper>
```

# 2. spu相关业务介绍

## 2.1 销售属性

销售属性，就是商品详情页右边，可以通过销售属性来定位一组spu下的哪款sku。

![](image/wps6_8sEWVLZ_-k.jpg)

因此，在制作spu之前要先确定当前商品有哪些销售属性！

## 2.2 spu数据结构图

![](image/wps7_AEfd8UGb5H.jpg)

# 3. 商品SPU列表功能

## 3.1 相关实体类

-   spu\_info： 商品SPU信息表
-   spu\_sale\_attr： spu销售属性表
-   spu\_sale\_attr\_value：spu销售属性值表
-   spu\_image：spu商品图片表
-   spu\_poster：spu商品海报表
-   base\_sale\_attr：基本销售属性表

## 3.2 控制器SpuManageController

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.model.SpuInfo;
import com.atguigu.gmall.product.service.SpuManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: atguigu
 * @create: 2022-12-24 14:11
 */
@RestController
@RequestMapping("/admin/product")
public class SpuManageController {

    @Autowired
    private SpuManageService spuManageService;

    /**
     * 分页查询商品SPU列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{page}/{size}")
    public Result getSpuByPage(@PathVariable("page") Long page, @PathVariable("size") Long size, @RequestParam(value = "category3Id", required = false) Long category3Id){
        IPage<SpuInfo> infoPage = new Page<>(page, size);
        infoPage = spuManageService.getSpuByPage(infoPage, category3Id);
        return Result.ok(infoPage);
    }
}
```

## 3.3 业务接口SpuManageService

```java
package com.atguigu.gmall.product.service;


import com.atguigu.gmall.product.model.SpuInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface SpuManageService {
    /**
     * 分页查询商品SPU列表
     * @param infoPage
     * @param category3Id
     * @return
     */
    IPage<SpuInfo> getSpuByPage(IPage<SpuInfo> infoPage, Long category3Id);
}
```

## 3.4 业务实现类 SpuManageServiceImpl

```java
package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.model.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: atguigu
 * @create: 2023-02-22 11:52
 */
@Service
public class SpuManageServiceImpl implements SpuManageService {

    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 分页查询商品SPU列表
     *
     * @param infoPage
     * @param category3Id
     * @return
     */
    @Override
    public IPage<SpuInfo> getSpuByPage(IPage<SpuInfo> infoPage, Long category3Id) {
        LambdaQueryWrapper<SpuInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (category3Id != null) {
            queryWrapper.eq(SpuInfo::getCategory3Id, category3Id);
        }
        queryWrapper.orderByDesc(SpuInfo::getUpdateTime);
        return spuInfoService.page(infoPage, queryWrapper);
    }
}
```

## 3.5 创建mapper

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.SpuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SpuInfoMapper extends BaseMapper<SpuInfo> {

}
```

# 4. Spu的保存功能中的图片上传

## 4.1 MinIO介绍

MinIO 是一个基于Apache License v3.0开源协议的对象存储服务。它兼容亚马逊S3云存储服务接口，非常适合于存储大容量非结构化的数据，例如图片、视频、日志文件、备份数据和容器/虚拟机镜像等，而一个对象文件可以是任意大小，从几kb到最大5T不等。

MinIO是一个非常轻量的服务,可以很简单的和其他应用的结合，类似 NodeJS, Redis 或者 MySQL。

官方文档：[http://docs.minio.org.cn/docs](http://docs.minio.org.cn/docs "http://docs.minio.org.cn/docs") 旧一点 中文

[https://docs.min.io/](https://docs.min.io/ "https://docs.min.io/") 新 英文

## 4.2 应用场景

### 4.2.1 单主机单硬盘模式

![](image/wps8_scaL1nq3av.jpg)

### 4.2.2 单主机多硬盘模式

![](image/wps9_doYhYR_bDx.jpg)

### 4.2.3 多主机多硬盘分布式

![](image/wps10_qh7jswmaZu.jpg)

## 4.3 特点

· 高性能：作为高性能对象存储，在标准硬件条件下它能达到55GB/s的读、35GB/s的写速率

· 可扩容：不同MinIO集群可以组成联邦，并形成一个全局的命名空间，并跨越多个数据中心

· 云原生：容器化、基于K8S的编排、多租户支持

· Amazon S3兼容：Minio使用Amazon S3 v2 / v4 API。可以使用Minio SDK，Minio Client，AWS SDK和AWS CLI访问Minio服务器。

· 可对接后端存储: 除了Minio自己的文件系统，还支持DAS、 JBODs、NAS、Google云存储和Azure Blob存储。

· SDK支持: 基于Minio轻量的特点，它得到类似Java、Python或Go等语言 的sdk支持

· Lambda计算: Minio服务器通过其兼容AWS SNS / SQS的事件通知服务触发Lambda功能。支持的目标是消息队列，如Kafka，NATS，AMQP，MQTT，Webhooks以及Elasticsearch，Redis，Postgres和MySQL等数据库。

· 有操作页面

· 功能简单: 这一设计原则让MinIO不容易出错、更快启动

· 支持纠删码：MinIO使用纠删码、Checksum来防止硬件错误和静默数据污染。在最高冗余度配置下，即使丢失1/2的磁盘也能恢复数据！

## 4.4 存储机制

Minio使用纠删码erasure code和校验和checksum。 即便丢失一半数量（N/2）的硬盘，仍然可以恢复数据。纠删码是一种恢复丢失和损坏数据的**数学算法**。

## 4.5 docker安装MinIO(已完成)

> docker pull minio/minio

> docker run \\
> \-p 9000:9000 \
> \-p 9001:9001 \\
> \--name minio \\
> \-d --restart=always \\
> \-e "MINIO\_ROOT\_USER=admin" \\
> \-e "MINIO\_ROOT\_PASSWORD=admin123456" \\
> \-v /home/data:/data \\
> \-v /home/config:/root/.minio \\
> minio/minio server /data --console-address ":9001"

浏览器访问：[http://IP:9001/minio/login，如图：](http://IP:9001/minio/login，如图： "http://IP:9001/minio/login，如图：")

![](image/wps11_v5uhthRIMp.jpg)

登录账户说明：安装时指定了**登录账号**：admin/admin123456

**注意**：文件上传时，需要调整一下linux 服务器的时间与windows 时间一致！

> 第一步：安装ntp服务
> yum -y install ntp
> 第二步：开启开机启动服务
> systemctl enable ntpd
> 第三步：启动服务
> systemctl start ntpd
> 第四步：更改时区
> timedatectl set-timezone Asia/Shanghai
> 第五步：启用ntp同步
> timedatectl set-ntp yes
> 第六步：同步时间
> ntpq -p

## 4.6  利用Java客户端调用Minio

参考文档：[https://docs.min.io/docs/java-client-api-reference.html](https://docs.min.io/docs/java-client-api-reference.html "https://docs.min.io/docs/java-client-api-reference.html")

### 4.6.1 引入依赖

在`service-product`模块中添加依赖

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.2.0</version>
</dependency>
```

### 4.6.2 添加配置信息

在nacos 配置中心列表中的`service-product-dev.yaml`增加以下信息！

```yaml
minio:
  endpointUrl: http://192.168.200.128:9000
  accessKey: admin
  secreKey: admin123456
  bucketName: gmall
```

### 4.6.3 创建FileUploadController控制器

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.UploadFileService;
import io.minio.*;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Api(tags = "文件上传")
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @SneakyThrows
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        //  调用服务层方法: ctrl+alt+m
        String url = uploadFileService.fileUpload(file);
        //  返回数据--上传之后的文件路径
        return Result.ok(url);
    }

    public static void main(String[] args) {
        //        int length = args.length; .8; 8-11 jpg;
        String fileName = "atgu.igu.jpg";

//        System.out.println(fileName.substring(fileName.lastIndexOf(".")));
//        System.out.println(fileName.substring(8,12));
        System.out.println(FilenameUtils.getExtension(fileName));
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));

    }

}
```

### 4.6.4 创建UploadFileService

```java
package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    String fileUpload(MultipartFile file);
}
```

### 4.6.5 创建FileUploadServiceImpl

```java
package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.service.UploadFileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RefreshScope
public class UploadFileServiceImpl implements UploadFileService {

    //  这种读取数据方式叫软编码！
    @Value("${minio.endpointUrl}")
    private String endpointUrl; // endpointUrl = http://192.168.200.130:9000
    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secreKey}")
    private String secreKey;

    @Override
    public String fileUpload(MultipartFile file) {
        String url = null;
        try {
            //  创建url 变量
            url = "";
            // 创建minioClient 客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpointUrl)
                            .credentials(accessKey, secreKey)
                            .build();

            // 判断桶是否存在。
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // 如果不存在，则创建
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                //  这个桶已经存在.
                System.out.println("Bucket "+ bucketName + " already exists.");
            }

            //  生成文件名。
            String fileName =  UUID.randomUUID().toString().replaceAll("-","")+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            //  调用上传方法.
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            //  endpointUrl = http://192.168.200.130:9000/gmalls/47ef53d6dc6e4e8b.jpg
            //  拼接url
            url=endpointUrl+"/"+bucketName+"/"+fileName;
            System.out.println(url);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}

```

# 5. spu保存

## 5.1 加载销售属性

需求：在添加SPU商品需要选择当前商品的销售属性

![](image/image-20221212234221124_AK5kjb1AtI.png)

> YAPI接口地址：[http://192.168.200.128:3000/project/11/interface/api/307](http://192.168.200.128:3000/project/11/interface/api/307 "http://192.168.200.128:3000/project/11/interface/api/307")

### 5.1.1 控制器BaseSaleAttrController

```java
package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: atguigu
 * @create: 2023-02-22 15:34
 */
@RestController
@RequestMapping("/admin/product")
public class BaseSaleAttrController {

    @Autowired
    private BaseSaleAttrService baseSaleAttrService;

    /**
     * 查询平台所有的销售属性名称
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result getBaseSaleAttrList(){
        return Result.ok(baseSaleAttrService.list());
    }
}
```

### 5.1.4. 创建mapper

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.BaseSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BaseSaleAttrMapper extends BaseMapper<BaseSaleAttr> {
}
```

## 5.3 保存后台代码

### 5.3.1 控制器SpuManageController

```java
/**
 * 保存商品SPU信息
 * @param spuInfo
 * @return
 */
@PostMapping("/saveSpuInfo")
public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
    spuManageService.saveSpuInfo(spuInfo);
    return Result.ok();
}
```

### 5.3.2 业务接口ManageService

```java
/**
 * 保存商品SPU信息
 * @param spuInfo
 * @return
 */
void saveSpuInfo(SpuInfo spuInfo);
```

### 5.3.3. 业务实现类ManageServiceImpl

```java
@Autowired
private SpuInfoMapper spuInfoMapper;

@Autowired
private SpuImageService spuImageService;

@Autowired
private SpuPosterService spuPosterService;

@Autowired
private SpuSaleAttrService spuSaleAttrService;

@Autowired
private SpuSaleAttrValueService spuSaleAttrValueService;

/**
 * 保存商品信息
 * 1.将商品基本新存入spu_info表,保存成功后得到商品SPUID
 * 2.获取前端提交商品图片 将图片列表存入spu_image表
 * 3.获取前端提交海报图片 将图片列表存入spu_poster表
 * 4.获取前端提交销售属性列表 将信息存入spu_sale_attr表
 * 5.获取前端提交销售属性值列表 将信息存入spu_sale_attr_val表
 *
 * @param spuInfo
 */
@Override
@Transactional(rollbackFor = Exception.class)
public void saveSpuInfo(SpuInfo spuInfo) {
    //1.将商品基本新存入spu_info表,保存成功后得到商品SpuId
    spuInfoService.save(spuInfo);
    //2.获取前端提交商品图片 将图片列表存入spu_image表
    List<SpuImage> spuImageList = spuInfo.getSpuImageList();
    if (!CollectionUtils.isEmpty(spuImageList)) {
        spuImageList.stream().forEach(spuImage -> {
            //2.1将商品图片关联到商品SPU
            spuImage.setSpuId(spuInfo.getId());
        });
        //2.2批量保存商品图片
        spuImageService.saveBatch(spuImageList);
    }
    //3.获取前端提交海报图片 将图片列表存入spu_poster表
    List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
    if (!CollectionUtils.isEmpty(spuPosterList)) {
        spuPosterList.stream().forEach(spuPoster -> {
            spuPoster.setSpuId(spuInfo.getId());
        });
        spuPosterService.saveBatch(spuPosterList);
    }
    //4.获取前端提交销售属性列表 将信息存入spu_sale_attr表
    List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
    if (!CollectionUtils.isEmpty(spuSaleAttrList)) {
        spuSaleAttrList.stream().forEach(spuSaleAttr -> {
            //4.1 关联到商品SPU
            spuSaleAttr.setSpuId(spuInfo.getId());
            //5.获取前端提交销售属性值列表 将信息存入spu_sale_attr_val表
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            if (!CollectionUtils.isEmpty(spuSaleAttrValueList)) {
                spuSaleAttrValueList.stream().forEach(spuSaleAttrValue -> {
                    //5.1 关联商品SPU
                    spuSaleAttrValue.setSpuId(spuInfo.getId());
                    //5.2 手动为销售属性名称赋值
                    spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                });
                //5.3 批量保存销售属性值
                spuSaleAttrValueService.saveBatch(spuSaleAttrValueList);
            }
        });
        //4.2 保存销售属性列表
        spuSaleAttrService.saveBatch(spuSaleAttrList);
    }
}
```

### 5.3.4 Mapper层

建立对应的mapper 文件

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.SpuImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SpuImageMapper extends BaseMapper<SpuImage> {
}
```

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

}
```

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.SpuSaleAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SpuSaleAttrValueMapper extends BaseMapper<SpuSaleAttrValue> {
}
```

```java
package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.model.SpuPoster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SpuPosterMapper extends BaseMapper<SpuPoster> {
}
```

### 5.3.5 创建service以及serviceImpl

```
public interface SpuImageService extends IService<SpuImage> {
}

public interface SpuPosterService extends IService<SpuPoster> {
}

public interface SpuSaleAttrService extends IService<SpuSaleAttr> {
}

public interface SpuSaleAttrValueService extends IService<SpuSaleAttrValue> {
}

@Service
public class SpuImageServiceImpl extends ServiceImpl<SpuImageMapper, SpuImage> implements SpuImageService {
}

@Service
public class SpuPosterServiceImpl extends ServiceImpl<SpuPosterMapper, SpuPoster> implements SpuPosterService {
}

@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr> implements SpuSaleAttrService {
}

@Service
public class SpuSaleAttrValueServiceImpl extends ServiceImpl<SpuSaleAttrValueMapper, SpuSaleAttrValue> implements SpuSaleAttrValueService {
}

```

