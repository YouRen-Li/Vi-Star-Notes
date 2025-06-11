package com.atguigu.order.controller;


import com.atguigu.order.service.OrderTblService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/orderTbl")
public class OrderTblController {
    @Resource
    OrderTblService orderTblService;
    @ApiOperation("创建订单")
    // skuId: 以后数据库中一个商品数据对应一个sku(stock keeping unit)
    @GetMapping("create/{skuId}/{userId}/{count}")
    public Boolean createOrder(@PathVariable("skuId")String skuId,
                               @PathVariable("userId")String userId,
                               @PathVariable("count")Integer count){
        return orderTblService.createOrder(skuId,userId,count);
    }
}

