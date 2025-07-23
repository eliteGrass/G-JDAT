package com.itsoku.lesson096;

import lombok.*;

/**
 * 订单对象
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderModel {
    /**
     * 订单id
     */
    private String id;
    /**
     * 订单状态
     */
    private Integer status;
}
