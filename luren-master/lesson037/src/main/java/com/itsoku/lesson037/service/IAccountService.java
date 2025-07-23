package com.itsoku.lesson037.service;

import com.itsoku.lesson037.common.service.IBaseService;
import com.itsoku.lesson037.po.AccountPO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 19:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IAccountService extends IBaseService<AccountPO> {

    /**
     * 减少账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    void balanceSubtract(String accountId, BigDecimal price);
}
