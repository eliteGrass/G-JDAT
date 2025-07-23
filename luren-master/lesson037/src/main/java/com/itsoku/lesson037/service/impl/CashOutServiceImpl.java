package com.itsoku.lesson037.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson037.common.BusinessExceptionUtils;
import com.itsoku.lesson037.config.RabbitMQConfiguration;
import com.itsoku.lesson037.dto.CashOutMsg;
import com.itsoku.lesson037.mapper.CashOutMapper;
import com.itsoku.lesson037.mq.sender.IMsgSender;
import com.itsoku.lesson037.po.CashOutPO;
import com.itsoku.lesson037.service.IAccountService;
import com.itsoku.lesson037.service.ICashOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/14 19:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
@Service
public class CashOutServiceImpl extends ServiceImpl<CashOutMapper, CashOutPO> implements ICashOutService {

    @Autowired
    private IMsgSender msgSender;

    @Autowired
    private IAccountService accountService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cashOut(String accountId, BigDecimal price) {
        //1、扣减账户余额
        this.accountService.balanceSubtract(accountId, price);

        //2、创建提现记录
        CashOutPO cashOutPO = this.createCashOutPO(accountId, price);

        //3、发送提现消息（必须是事务消息）
        this.sendCashOutMsg(cashOutPO);
        return cashOutPO.getId();
    }

    @Override
    public void disposeCashOutMsg(CashOutMsg cashOutMsg) {
        String cashOutId = cashOutMsg.getCashOutId();
        CashOutPO cashOutPO = this.getById(cashOutId);
        if (cashOutPO == null) {
            throw BusinessExceptionUtils.businessException("提现记录不存在");
        }
        //若处理成功了，直接返回
        if (cashOutPO.getStatus() == 100) {
            return;
        }

        /**
         * 1、调用微信转账到接口，打款到用户的个人钱包
         */
        this.wechatTransfer(cashOutPO);

        /**
         * 2、将提现记录状态置为成功
         */
        this.cashOutSuccess(cashOutId);
    }

    private void cashOutSuccess(String cashOutId) {
        this.transactionTemplate.executeWithoutResult(action -> {
            // update t_cash_out set status = 100, update_time = #{当前时间} where id = #{cashOutId} and status = 0
            LambdaUpdateWrapper<CashOutPO> updateWrapper = Wrappers.lambdaUpdate(CashOutPO.class)
                    .eq(CashOutPO::getId, cashOutId)
                    .eq(CashOutPO::getStatus, 0)
                    .set(CashOutPO::getStatus, 100)
                    .set(CashOutPO::getUpdateTime, LocalDateTime.now());
            if (!this.update(updateWrapper)) {
                log.error("更新转账记录失败:{}", JSONUtil.toJsonStr(this.getById(cashOutId)));
                throw BusinessExceptionUtils.businessException("更新转账记录失败");
            }

            //若还有其他操作，可以写到这里
        });
    }

    /**
     * 调用微信转账到接口，打款到用户的个人钱包
     *
     * @param cashOutPO
     */

    private void wechatTransfer(CashOutPO cashOutPO) {
        //此处调用微信转账接口，微信转账接口是幂等的，只要是同一个订单号，支持多次调用

    }

    private void sendCashOutMsg(CashOutPO cashOutPO) {
        CashOutMsg cashOutMsg = CashOutMsg.builder().cashOutId(cashOutPO.getId()).build();
        this.msgSender.sendWithBody(RabbitMQConfiguration.CashOut.EXCHANGE,
                RabbitMQConfiguration.CashOut.ROUTING_KEY,
                cashOutMsg);
    }

    private CashOutPO createCashOutPO(String accountId, BigDecimal price) {
        CashOutPO cashOutPO = new CashOutPO();
        cashOutPO.setId(IdUtil.fastSimpleUUID());
        cashOutPO.setAccountId(accountId);
        cashOutPO.setPrice(price);
        cashOutPO.setStatus(0); //待处理
        cashOutPO.setCreateTime(LocalDateTime.now());
        this.save(cashOutPO);
        return cashOutPO;
    }
}
