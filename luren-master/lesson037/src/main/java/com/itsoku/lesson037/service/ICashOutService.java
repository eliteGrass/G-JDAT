package com.itsoku.lesson037.service;

import com.itsoku.lesson037.common.service.IBaseService;
import com.itsoku.lesson037.dto.CashOutMsg;
import com.itsoku.lesson037.po.AccountPO;
import com.itsoku.lesson037.po.CashOutPO;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 19:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ICashOutService extends IBaseService<CashOutPO> {
    /**
     * 用户申请提现到微信钱包
     *
     * @param accountId 账号id
     * @param price     提现金额
     * @return
     */
    String cashOut(String accountId, BigDecimal price);

    /**
     * 处理提现消息
     * @param cashOutMsg
     */
    void disposeCashOutMsg(CashOutMsg cashOutMsg);
}
