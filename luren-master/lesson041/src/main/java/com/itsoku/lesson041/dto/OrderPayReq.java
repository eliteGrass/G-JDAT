package com.itsoku.lesson041.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/25 13:05 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class OrderPayReq {
    /**
     * 订单id
     */
    @NotBlank(message = "订单id不能为空")
    private String orderId;
}
