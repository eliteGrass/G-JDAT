package com.itsoku.lesson048.service;

import com.itsoku.lesson048.common.service.IBaseService;
import com.itsoku.lesson048.enuns.AccountFundsDataBusTypeEnum;
import com.itsoku.lesson048.po.AccountFundsPO;

import java.math.BigDecimal;

/**
 * 账户资金服务
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 12:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IAccountFundsService extends IBaseService<AccountFundsPO> {
    /**
     * 余额增加，逻辑如下:
     * 1、更新账户资金（balance = balance + #{price})
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void balanceAdd(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 余额减少，逻辑如下:
     * 1、更新账户资金（balance = balance - #{price})
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void balanceSubtract(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 冻结：将余额转入冻结金额中，逻辑如下
     * 1、更新账户资金（balance = balance-price，frozen = frozen+price）
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void frozen(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 解冻：将冻结金额转入余额中，逻辑如下
     * 1、更新账户资金（frozen = frozen-#{price}，balance = balance+{price}）
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void unFrozen(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 冻结金额增加，逻辑如下:
     * 1、更新账户资金（frozen = frozen + #{price})
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void frozenAdd(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 冻结金额减少，逻辑如下:
     * 1、更新账户资金（frozen = frozen - #{price})
     * 2、并写入资金流水
     *
     * @param accountId   用户账号id
     * @param price       金额
     * @param busTypeEnum 流水关联的业务类型
     * @param busId       流水关联的业务id
     */
    void frozenSubtract(Long accountId, BigDecimal price, AccountFundsDataBusTypeEnum busTypeEnum, Long busId);

    /**
     * 获取用户账户资金信息
     *
     * @param accountId
     * @return
     */
    AccountFundsPO get(Long accountId);
}
