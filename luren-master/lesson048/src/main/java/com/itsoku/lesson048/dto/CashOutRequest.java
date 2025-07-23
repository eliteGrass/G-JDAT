package com.itsoku.lesson048.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class CashOutRequest {
    /**
     * 提现账号id
     */
    private Long accountId;
    /**
     * 提现金额
     */
    private BigDecimal price;
}
