package com.atguigu.business.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value ="storage-service" , url = "http://localhost:8112")
public interface StorageClient {
    @ApiOperation("扣库存")
    @GetMapping("storageTbl/debit/{skuId}/{count}")
    public Boolean debit(@PathVariable("skuId")String skuId,
                         @PathVariable("count")Integer count);
}
