package com.itsoku.lesson038.service2.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class TransferRequest {
    //付款人账户id
    private String fromAccountId;
    //收款人账号id
    private String toAccountId;
    //转账金额
    private BigDecimal transferPrice;
}
