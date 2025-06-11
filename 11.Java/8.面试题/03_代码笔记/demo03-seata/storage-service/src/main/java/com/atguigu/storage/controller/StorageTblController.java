package com.atguigu.storage.controller;


import com.atguigu.storage.service.StorageTblService;
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
@RequestMapping("/storageTbl")
public class StorageTblController {
    @Resource
    StorageTblService storageTblService;
    @ApiOperation("扣库存")
    @GetMapping("debit/{skuId}/{count}")
    public Boolean debit(@PathVariable("skuId")String skuId,
                         @PathVariable("count")Integer count){

        return storageTblService.debit(skuId,count);
    }
}

