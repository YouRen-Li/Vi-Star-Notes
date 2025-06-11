package com.atguigu.storage.service.impl;

import com.atguigu.storage.client.AccountClient;
import com.atguigu.storage.entity.StorageTbl;
import com.atguigu.storage.mapper.StorageTblMapper;
import com.atguigu.storage.service.StorageTblService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
@Service
public class StorageTblServiceImpl extends ServiceImpl<StorageTblMapper, StorageTbl> implements StorageTblService {
    @Resource
    AccountClient accountClient;
    @Transactional
    @Override
    public Boolean debit(String skuId, Integer count) {
        StorageTbl storageTbl = this.getOne(Wrappers.lambdaQuery(StorageTbl.class)
                .eq(StorageTbl::getCommodityCode, skuId));
        if(storageTbl==null){
            return false;
        }
        storageTbl.setCount(storageTbl.getCount()-count);
        this.updateById(storageTbl);

        //扣除账户余额:userId在数据库表中必须存在
        accountClient.updateAccount("1001",count*50,new Random().nextInt(10000)+"");

        return true;
    }
}
