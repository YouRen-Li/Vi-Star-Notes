package com.atguigu.account.controller;


import com.atguigu.account.service.AccountTblService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/accountTbl")
public class AccountTblController {
    @Resource
    AccountTblService accountTblService;
    @ApiOperation("修改账户余额")
    @GetMapping("update/{userId}/{amount}/{orderId}")
    public Boolean updateAccount(@PathVariable("userId")String userId,
                                 @PathVariable("amount")Integer amount,
                                 @PathVariable("orderId")String orderId){
        return accountTblService.updateAccount(userId,amount,orderId);
    }
}

