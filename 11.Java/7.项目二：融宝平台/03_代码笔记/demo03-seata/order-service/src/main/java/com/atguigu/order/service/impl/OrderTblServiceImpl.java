package com.atguigu.order.service.impl;

import com.atguigu.order.entity.OrderTbl;
import com.atguigu.order.mapper.OrderTblMapper;
import com.atguigu.order.service.OrderTblService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
@Service
public class OrderTblServiceImpl extends ServiceImpl<OrderTblMapper, OrderTbl> implements OrderTblService {
    @Override
    @Transactional
    public Boolean createOrder(String skuId, String userId, Integer count) {
        OrderTbl order = new OrderTbl();
        order.setCommodityCode(skuId);
        order.setUserId(userId);
        order.setCount(count);
        order.setMoney(count*50);
        return this.save(order);
    }
}
