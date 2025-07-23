package com.itsoku.lesson096;

/**
 * 订单状态变更动作
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum OrderStatusChangeAction {
    PAY, //支付
    SHIP, //发货
    DELIVER //确认收货
}
