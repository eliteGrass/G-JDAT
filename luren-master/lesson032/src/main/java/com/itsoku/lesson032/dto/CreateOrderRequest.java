package com.itsoku.lesson032.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class CreateOrderRequest {

    @NotBlank(message = "商品不能为空")
    private String goods;

    @NotNull(message = "订单金额")
    private BigDecimal price;

    /**
     * 投递的消息延迟时间
     */
    private long delaySeconds;
}
