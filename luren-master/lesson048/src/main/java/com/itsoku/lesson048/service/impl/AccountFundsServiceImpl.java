package com.itsoku.lesson048.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson048.common.BusinessExceptionUtils;
import com.itsoku.lesson048.enuns.AccountFundsDataBusTypeEnum;
import com.itsoku.lesson048.enuns.AccountFundsDataIncomeEnum;
import com.itsoku.lesson048.mapper.AccountFundsMapper;
import com.itsoku.lesson048.po.AccountFundsPO;
import com.itsoku.lesson048.po.AccountFundsSnapshotPO;
import com.itsoku.lesson048.service.IAccountFundsDataService;
import com.itsoku.lesson048.service.IAccountFundsService;
import com.itsoku.lesson048.service.IAccountFundsSnapshotService;
import com.itsoku.lesson048.utils.ArithUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 13:06 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class AccountFundsServiceImpl extends ServiceImpl<AccountFundsMapper, AccountFundsPO> implements IAccountFundsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountFundsServiceImpl.class);

    @Autowired
    private IAccountFundsDataService accountFundsDataService;

    @Autowired
    private IAccountFundsSnapshotService accountFundsSnapshotService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void balanceAdd(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.IN, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    // 余额增加
                    accountFundsPO.setBalance(ArithUtils.add(accountFundsPO.getBalance(), amount));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void balanceSubtract(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.OUT, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    //余额不足
                    if (ArithUtils.lt(accountFundsPO.getBalance(), amount)) {
                        LOGGER.warn("余额不足,accountId：{}，balance：{}，price：{}", accountId, accountFundsPO.getBalance(), amount);
                        throw BusinessExceptionUtils.businessException("余额不足");
                    }
                    // 余额减少
                    accountFundsPO.setBalance(ArithUtils.sub(accountFundsPO.getBalance(), amount));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void frozen(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.FIXED, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    //余额不足
                    if (ArithUtils.lt(accountFundsPO.getBalance(), amount)) {
                        LOGGER.warn("余额不足,accountId：{}，balance：{}，price：{}", accountId, accountFundsPO.getBalance(), amount);
                        throw BusinessExceptionUtils.businessException("余额不足");
                    }
                    // 余额减少
                    accountFundsPO.setBalance(ArithUtils.sub(accountFundsPO.getBalance(), amount));
                    //冻结金额增加
                    accountFundsPO.setFrozen(ArithUtils.add(accountFundsPO.getFrozen(), amount));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unFrozen(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.FIXED, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    //冻结金额不足
                    if (ArithUtils.lt(accountFundsPO.getFrozen(), amount)) {
                        LOGGER.warn("冻结金额不足,accountId：{}，frozen：{}，price：{}", accountId, accountFundsPO.getFrozen(), amount);
                        throw BusinessExceptionUtils.businessException("冻结金额不足");
                    }
                    // 冻结金额减少
                    accountFundsPO.setFrozen(ArithUtils.sub(accountFundsPO.getFrozen(), amount));
                    //余额增加
                    accountFundsPO.setBalance(ArithUtils.add(accountFundsPO.getBalance(), amount));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void frozenAdd(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.IN, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    // 冻结金额增加
                    accountFundsPO.setFrozen(ArithUtils.add(accountFundsPO.getFrozen(), amount));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void frozenSubtract(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        this.updateFunds(AccountFundsDataIncomeEnum.OUT, accountId, price, accountFundsDataBusTypeEnum, busId,
                (accountFundsPO, amount) -> {
                    //冻结金额不足
                    if (ArithUtils.lt(accountFundsPO.getFrozen(), amount)) {
                        LOGGER.warn("冻结金额不足,accountId：{}，frozen：{}，price：{}", accountId, accountFundsPO.getFrozen(), amount);
                        throw BusinessExceptionUtils.businessException("冻结金额不足");
                    }
                    // 余额减少
                    accountFundsPO.setFrozen(ArithUtils.sub(accountFundsPO.getFrozen(), amount));
                });
    }

    /**
     * 更新账户资金 & 记录资金流水
     *
     * @param expectedIncome              期望income的值
     * @param accountId                   账户
     * @param price                       交易金额
     * @param accountFundsDataBusTypeEnum 交易类型
     * @param busId                       交易关联的表的主键id
     * @param updateFundsFunction         调用方更新账户资金相关的字段（内部自包含计算，不包含持久化到db）
     */
    private void updateFunds(AccountFundsDataIncomeEnum expectedIncome, Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId, BiConsumer<AccountFundsPO, BigDecimal> updateFundsFunction) {
        if (price == null || ArithUtils.lt0(price)) {
            throw BusinessExceptionUtils.businessException("price必须大于0");
        }

        if (expectedIncome == null || !Objects.equals(accountFundsDataBusTypeEnum.getIncome(), expectedIncome)) {
            throw BusinessExceptionUtils.businessException(String.format("income有误，期望值：%s，实际值：%s", accountFundsDataBusTypeEnum.getIncome().getValue(), expectedIncome.getValue()));
        }

        AccountFundsPO accountFundsPO = this.get(accountId);

        //交易前账户资金快照
        AccountFundsSnapshotPO beforeAccountFundsSnapshotPO = this.accountFundsSnapshotService.insert(accountFundsPO);

        //更新账户资金（内部自包含计算，不包含持久化到db）
        updateFundsFunction.accept(accountFundsPO, price);
        accountFundsPO.setUpdateTime(LocalDateTime.now());

        //乐观锁更新账户资金
        boolean updateFlag = this.updateById(accountFundsPO);
        if (!updateFlag) {
            LOGGER.warn("更新账户失败:{}", accountFundsPO);
            throw BusinessExceptionUtils.businessException("系统繁忙，请稍后重试!");
        }

        // 交易后账户资金快照
        AccountFundsSnapshotPO afterAccountFundsSnapshotPO = this.accountFundsSnapshotService.insert(this.get(accountId));

        //记录流水
        this.accountFundsDataService.insert(
                accountId,
                price,
                beforeAccountFundsSnapshotPO.getId(),
                afterAccountFundsSnapshotPO.getId(),
                accountFundsDataBusTypeEnum,
                busId);
    }

    @Override
    public AccountFundsPO get(Long accountId) {
        return this.getById(accountId);
    }
}
