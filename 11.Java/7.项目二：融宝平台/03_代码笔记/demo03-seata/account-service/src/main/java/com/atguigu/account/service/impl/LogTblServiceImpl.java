package com.atguigu.account.service.impl;

import com.atguigu.account.entity.LogTbl;
import com.atguigu.account.mapper.LogTblMapper;
import com.atguigu.account.service.LogTblService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
@Service
public class LogTblServiceImpl extends ServiceImpl<LogTblMapper, LogTbl> implements LogTblService {

    @Transactional(rollbackFor = Exception.class)//(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Integer money,String orderId){
        LogTbl logTbl = new LogTbl();
        logTbl.setGmtCreate(new Date());
        logTbl.setGmtModified(new Date());
        logTbl.setMoney(money);
        logTbl.setOrderId(orderId);
        this.save(logTbl);
    }
}
