package com.atguigu.business.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value ="order-service" , url = "http://localhost:8113")
public interface OrderClient {
    @ApiOperation("创建订单")
    // skuId: 以后数据库中一个商品数据对应一个sku(stock keeping unit)
    @GetMapping("orderTbl/create/{skuId}/{userId}/{count}")
    public Boolean createOrder(@PathVariable("skuId")String skuId,
                               @PathVariable("userId")String userId,
                               @PathVariable("count")Integer count);
}
