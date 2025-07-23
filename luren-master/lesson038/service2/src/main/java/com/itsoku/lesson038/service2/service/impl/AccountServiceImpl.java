package com.itsoku.lesson038.service2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson038.service2.mapper.AccountMapper;
import com.itsoku.lesson038.service2.po.AccountPO;
import com.itsoku.lesson038.service2.service.IAccountService;
import com.itsoku.utils.FrameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/14 19:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountPO> implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;


    /**
     * 增加账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void balanceAdd(String accountId, BigDecimal price) {
        int update = this.accountMapper.balanceAdd(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("更新账号余额失败!");
        }
    }

    /**
     * 减少账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void balanceSubtract(String accountId, BigDecimal price) {
        int update = this.accountMapper.balanceSubtract(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("账户余额不足!");
        }
    }

    @Override
    public void frozen(String accountId, BigDecimal price) {
        int update = this.accountMapper.frozen(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("账户金额冻结失败");
        }
    }

    @Override
    public void unFrozen(String accountId, BigDecimal price) {
        int update = this.accountMapper.unFrozen(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("账户金额解冻失败");
        }
    }

    @Override
    public void frozenAdd(String accountId, BigDecimal price) {
        int update = this.accountMapper.frozenAdd(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("冻结金额更新异常");
        }
    }

    @Override
    public void frozenSubtract(String accountId, BigDecimal price) {
        int update = this.accountMapper.frozenSubtract(accountId, price);
        if (update != 1) {
            FrameUtils.throwBaseException("冻结金额更新异常");
        }
    }
}
