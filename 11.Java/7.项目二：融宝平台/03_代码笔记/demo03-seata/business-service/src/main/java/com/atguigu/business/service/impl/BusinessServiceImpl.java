package com.atguigu.business.service.impl;

import com.atguigu.business.client.OrderClient;
import com.atguigu.business.client.StorageClient;
import com.atguigu.business.service.BusinessService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class BusinessServiceImpl implements BusinessService {
    @Resource
    OrderClient orderClient;
    @Resource
    StorageClient storageClient;
//    @Transactional
    //下单入口业务：
    @GlobalTransactional //入口业务 开启全局分布式事务
    @Override
    public Boolean buy(String userId, String skuId, Integer count) {
        //1、远程访问订单服务 下单
        orderClient.createOrder(skuId,userId,count);
        //2、远程访问库存服务减库存
        // 减库存时 远程访问账户服务 扣余额(有异常)
        storageClient.debit(skuId,count);
        return true;
    }
}
