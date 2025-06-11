package com.atguigu.storage.service;

import com.atguigu.storage.entity.StorageTbl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
public interface StorageTblService extends IService<StorageTbl> {

    Boolean debit(String skuId, Integer count);
}
