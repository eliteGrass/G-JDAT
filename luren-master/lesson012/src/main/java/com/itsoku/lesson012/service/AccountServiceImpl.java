package com.itsoku.lesson012.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson012.comm.ServiceExceptionUtils;
import com.itsoku.lesson012.mapper.AccountMapper;
import com.itsoku.lesson012.po.AccountPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountPO> implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void balanceAdd(String accountId, BigDecimal price) {
        int updateCount = accountMapper.balanceAdd(accountId, price);
        if (updateCount != 1) {
            throw ServiceExceptionUtils.exception("更新账户余额失败!");
        }
    }
}
