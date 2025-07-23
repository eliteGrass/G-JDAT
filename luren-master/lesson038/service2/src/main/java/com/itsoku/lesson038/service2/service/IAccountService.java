package com.itsoku.lesson038.service2.service;

import com.itsoku.common.service.IBaseService;
import com.itsoku.lesson038.service2.po.AccountPO;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 19:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IAccountService extends IBaseService<AccountPO> {
    /**
     * 余额增加price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void balanceAdd(String accountId, BigDecimal price);

    /**
     * 余额减少price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void balanceSubtract(String accountId, BigDecimal price);

    /**
     * 冻结：将余额转入冻结金额中（balance-price，frozen+price）
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void frozen(String accountId, BigDecimal price);

    /**
     * 解冻：将冻结金额转入余额中（frozen-price，balance+price）
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void unFrozen(String accountId, BigDecimal price);

    /**
     * 冻结金额增加price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void frozenAdd(String accountId, BigDecimal price);

    /**
     * 冻结金额减少price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    void frozenSubtract(String accountId, BigDecimal price);

}
