package com.atguigu.account.service;

import com.atguigu.account.entity.LogTbl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
public interface LogTblService extends IService<LogTbl> {

    void saveLog(Integer amount, String orderId);
}
