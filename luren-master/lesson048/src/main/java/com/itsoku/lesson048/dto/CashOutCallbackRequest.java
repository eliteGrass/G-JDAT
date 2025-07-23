package com.itsoku.lesson048.dto;

import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 14:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class CashOutCallbackRequest {
    /**
     * 提现记录id
     */
    private Long cashOutId;

    /**
     * 成功？
     */
    private boolean success;

    /**
     * 失败原因
     */
    private String failMsg;
}
