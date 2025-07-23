package com.itsoku.lesson037.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 15:14 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@Builder
public class CashOutMsg {
    //提现记录id
    private String cashOutId;
}
