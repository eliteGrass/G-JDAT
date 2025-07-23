package com.itsoku.lesson059.pay;

/**
 * 支付策略接口
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/4 20:27 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface PayStrategy {
    /**
     * 当前策略对应的支付渠道
     *
     * @return
     */
    String payChannel();

    /**
     * 具体的支付逻辑代码
     *
     * @param orderNo
     * @return
     */
    String pay(String orderNo);
}
