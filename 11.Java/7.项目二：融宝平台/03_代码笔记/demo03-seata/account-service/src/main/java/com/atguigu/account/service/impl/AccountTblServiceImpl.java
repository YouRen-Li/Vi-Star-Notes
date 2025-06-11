package com.atguigu.account.service.impl;

import com.atguigu.account.entity.AccountTbl;
import com.atguigu.account.entity.LogTbl;
import com.atguigu.account.mapper.AccountTblMapper;
import com.atguigu.account.service.AccountTblService;
import com.atguigu.account.service.LogTblService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class AccountTblServiceImpl extends ServiceImpl<AccountTblMapper, AccountTbl> implements AccountTblService {
    @Resource
    LogTblService logTblService;
//    @Resource
//    AccountTblServiceImpl accountTblService;
    /*
        spring事务传播行为 为什么会失效？
            Transactional注解基于aop，如果调用方法的对象不是aop容器中获取的被代理的对象 事务传播行为会失效
            解决：添加事务注解的方法 由容器中装配的对象调用
     */
    //事务传播行为
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class) //需要事务
    @Override
    public Boolean updateAccount(String userId, Integer amount,String orderId) {
        //更新账户金额
        AccountTbl accountTbl = this.getOne(Wrappers.lambdaQuery(AccountTbl.class)
                .eq(AccountTbl::getUserId, userId));
        if(accountTbl==null){
            return false;
        }
        accountTbl.setMoney(accountTbl.getMoney()-amount);
        boolean b = this.updateById(accountTbl);
        //保存日志
        this.logTblService.saveLog(amount,orderId);
        //saveLog提交   updateAccount回滚
        int i = 1/0;
        return b;
    }
    //挂起上一个事务  创建新事务执行

}
