package com.atguigu.storage.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "account-service",url = "http://localhost:8111")
public interface AccountClient {
    @ApiOperation("修改账户余额")
    @GetMapping("accountTbl/update/{userId}/{amount}/{orderId}")
    public Boolean updateAccount(@PathVariable("userId")String userId,
                                 @PathVariable("amount")Integer amount,
                                 @PathVariable("orderId")String orderId);
}
