package com.atguigu.business.controller;

import com.atguigu.business.service.BusinessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("business")
public class BusinessController {
    @Resource
    BusinessService businessService;
    @ApiOperation("下单")
    @GetMapping("buy/{userId}/{skuId}/{count}")
    public Boolean buy(@PathVariable("userId")String userId,
                       @PathVariable("skuId")String skuId,
                       @PathVariable("count")Integer count){
        return businessService.buy(userId,skuId,count);
    }
}
