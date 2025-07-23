package com.itsoku.lesson067.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/22 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    //付款人账户id
    private String fromAccountId;
    //收款人账号id
    private String toAccountId;
    //转账金额
    private BigDecimal transferPrice;
}
