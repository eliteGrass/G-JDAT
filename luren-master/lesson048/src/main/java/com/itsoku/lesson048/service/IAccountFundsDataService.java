package com.itsoku.lesson048.service;

import com.itsoku.lesson048.common.service.IBaseService;
import com.itsoku.lesson048.enuns.AccountFundsDataBusTypeEnum;
import com.itsoku.lesson048.po.AccountFundsDataPO;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 12:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IAccountFundsDataService extends IBaseService<AccountFundsDataPO> {
    /**
     * 插入一条流水记录
     *
     * @param accountId                    账号id
     * @param price                        交易金额
     * @param beforeAccountFundsSnapshotId 交易前资金快照id
     * @param afterAccountFundsSnapshotId  交易后资金快照id
     * @param accountFundsDataBusTypeEnum  交易类型
     * @param busId                        交易关联的业务对象id
     */
    void insert(Long accountId, BigDecimal price, Long beforeAccountFundsSnapshotId, Long afterAccountFundsSnapshotId, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId);
}
