package com.itsoku.lesson036.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson036.common.BusinessExceptionUtils;
import com.itsoku.lesson036.config.RabbitMQConfiguration;
import com.itsoku.lesson036.dto.TransferMsg;
import com.itsoku.lesson036.mapper.AccountMapper;
import com.itsoku.lesson036.mq.sender.IMsgSender;
import com.itsoku.lesson036.po.AccountPO;
import com.itsoku.lesson036.service.IAccountService;
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

    @Autowired
    private IMsgSender msgSender;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String fromAccountId, String toAccountId, BigDecimal transferPrice) {
        //1、转账人，账户余额减少
        this.balanceSubtract(fromAccountId, transferPrice);

        //2、投递MQ转账消息
        TransferMsg msg = TransferMsg.builder().
                fromAccountId(fromAccountId).
                toAccountId(toAccountId).
                transferPrice(transferPrice).build();
        this.msgSender.sendWithBody(RabbitMQConfiguration.Transfer.EXCHANGE,
                RabbitMQConfiguration.Transfer.ROUTING_KEY,
                msg);
    }

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
            throw BusinessExceptionUtils.businessException("更新账号余额失败!");
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
            throw BusinessExceptionUtils.businessException("更新账号余额失败!");
        }
    }
}
