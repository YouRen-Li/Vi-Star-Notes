package com.atguigu.account.service;

import com.atguigu.account.entity.AccountTbl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
public interface AccountTblService extends IService<AccountTbl> {

    Boolean updateAccount(String userId, Integer amount,String orderId);
}
