package com.itsoku.lesson048.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson048.common.BusinessExceptionUtils;
import com.itsoku.lesson048.dto.CashOutCallbackRequest;
import com.itsoku.lesson048.enuns.AccountFundsDataBusTypeEnum;
import com.itsoku.lesson048.enuns.CashOutStatusEnum;
import com.itsoku.lesson048.mapper.CashOutMapper;
import com.itsoku.lesson048.po.CashOutPO;
import com.itsoku.lesson048.service.IAccountFundsService;
import com.itsoku.lesson048.service.ICashOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private IAccountFundsService accountFundsService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public Long cashOut(Long accountId, BigDecimal price) {
        /**
         * 1、扣减余额并创建提现记录
         */
        CashOutPO cashOutPO = this.createCashOut(accountId, price);

        /**
         * todo: 2、这里调用第三方提现接口发起提现，可以在这里执行，也可以异步去执行
         */

        /**
         * 告诉用户提现已发起，正在处理中
         */
        return cashOutPO.getId();
    }

    @Override
    public void cashOutCallback(CashOutCallbackRequest request) {
        CashOutPO cashOutPO = this.getById(request.getCashOutId());
        if (cashOutPO == null) {
            throw BusinessExceptionUtils.businessException("提现记录不存在");
        }
        //提现记录状态为成功或者失败，说明已处理过了，直接返回
        if (cashOutPO.getStatus() == 1 || cashOutPO.getStatus() == 2) {
            return;
        }
        if (request.isSuccess()) {
            this.cashOutSuccess(cashOutPO);
        } else {
            this.cashOutFail(cashOutPO, request.getFailMsg());
        }
    }

    private void cashOutSuccess(CashOutPO cashOutPO) {
        //放在事务中执行
        this.transactionTemplate.executeWithoutResult(action -> {
            //1、更新提现记录状态为成功状态：update status = 1, update_time = now() where id = #{cashOutPO.id} and status = 0
            LambdaUpdateWrapper<CashOutPO> updateWrapper = Wrappers.lambdaUpdate(CashOutPO.class)
                    .set(CashOutPO::getStatus, CashOutStatusEnum.SUCCESS.getValue())
                    .set(CashOutPO::getUpdateTime, LocalDateTime.now())
                    .eq(CashOutPO::getId, cashOutPO.getId())
                    .eq(CashOutPO::getStatus, 0);
            if (!this.update(updateWrapper)) {
                log.error("更新提现记录状态异常，request：{}，{}", JSONUtil.toJsonStr(cashOutPO));
                throw BusinessExceptionUtils.businessException("更新提现记录状态异常");
            }
            //2、更新账户资金：扣除冻结金额（冻结金额 = 冻结金额 - 提现金额）
            this.accountFundsService.frozenSubtract(cashOutPO.getAccountId(),
                    cashOutPO.getPrice(),
                    AccountFundsDataBusTypeEnum.CASH_OUT_SUCCESS,
                    cashOutPO.getId());
        });
    }

    private void cashOutFail(CashOutPO cashOutPO, String failMsg) {
        //放在事务中执行
        this.transactionTemplate.executeWithoutResult(action -> {
            //1、更新提现记录状态为失败状态：update status = 2, fail_msg = #{failMsg}, update_time = now() where id = #{cashOutPO.id} and status = 0
            LambdaUpdateWrapper<CashOutPO> updateWrapper = Wrappers.lambdaUpdate(CashOutPO.class)
                    .set(CashOutPO::getStatus, CashOutStatusEnum.FAIL.getValue())
                    .set(CashOutPO::getFailMsg, failMsg)
                    .set(CashOutPO::getUpdateTime, LocalDateTime.now())
                    .eq(CashOutPO::getId, cashOutPO.getId())
                    .eq(CashOutPO::getStatus, 0);
            if (!this.update(updateWrapper)) {
                log.error("更新提现记录状态异常，request：{}，{}", JSONUtil.toJsonStr(cashOutPO));
                throw BusinessExceptionUtils.businessException("更新提现记录状态异常");
            }
            //2、更新账户资金：资金从冻结金额流向余额（冻结金额 = 冻结金额 - 提现金额，余额 = 余额 + 提现金额）
            this.accountFundsService.unFrozen(cashOutPO.getAccountId(),
                    cashOutPO.getPrice(),
                    AccountFundsDataBusTypeEnum.CASH_OUT_FAIL,
                    cashOutPO.getId());
        });
    }

    private CashOutPO createCashOut(Long accountId, BigDecimal price) {
        return this.transactionTemplate.execute(action -> {
            //1、创建提现记录
            CashOutPO cashOutPO = new CashOutPO();
            cashOutPO.setAccountId(accountId);
            cashOutPO.setPrice(price);
            cashOutPO.setStatus(0); //待处理
            cashOutPO.setCreateTime(LocalDateTime.now());
            this.save(cashOutPO);

            //2、更新账户资金，资金从余额流向冻结金额（余额 = 余额 - 提现金额，冻结金额 = 冻结金额 + 提现金额）
            this.accountFundsService.frozen(accountId,
                    price,
                    AccountFundsDataBusTypeEnum.CASH_OUT_FROZEN,
                    cashOutPO.getId());
            return cashOutPO;
        });
    }


}
