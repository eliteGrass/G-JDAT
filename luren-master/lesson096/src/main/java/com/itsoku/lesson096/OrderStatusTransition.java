package com.itsoku.lesson096;

import lombok.*;

/**
 * 订单状态流转对象，表示订单从一个状态（fromStatus)，触发了动作（action），然后转换到另外一个状态（toStatus)
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusTransition {
    /**
     * 当前状态
     */
    private OrderStatus fromStatus;
    /**
     * 动作
     */
    private OrderStatusChangeAction action;
    /**
     * 目标状态
     */
    private OrderStatus toStatus;
}
