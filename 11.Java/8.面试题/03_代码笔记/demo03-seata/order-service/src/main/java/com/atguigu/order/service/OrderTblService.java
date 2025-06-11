package com.atguigu.order.service;

import com.atguigu.order.entity.OrderTbl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
public interface OrderTblService extends IService<OrderTbl> {

    Boolean createOrder(String skuId, String userId, Integer count);
}
