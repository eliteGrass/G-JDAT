package com.itsoku.lesson048.service;

import com.itsoku.lesson048.common.service.IBaseService;
import com.itsoku.lesson048.po.AccountFundsPO;
import com.itsoku.lesson048.po.AccountFundsSnapshotPO;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 12:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IAccountFundsSnapshotService extends IBaseService<AccountFundsSnapshotPO> {
    /**
     * 插入一条流水记录
     */
    AccountFundsSnapshotPO insert(AccountFundsPO accountFundsPO);
}
