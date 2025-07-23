package com.itsoku.lesson059.service;

import com.itsoku.lesson059.pay.PayStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用策略模式实现的支付服务
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/5 10:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class NewPayService {
    // 支付渠道  -> 具体的支付策略
    private Map<String, PayStrategy> payStrategyMap;

    public NewPayService(List<PayStrategy> payStrategyList) {
        this.payStrategyMap = new HashMap<>();
        for (PayStrategy payStrategy : payStrategyList) {
            this.payStrategyMap.put(payStrategy.payChannel(), payStrategy);
        }
    }

    /**
     * 支付
     *
     * @param channel
     * @param orderNo
     * @return
     */
    public String pay(String channel, String orderNo) {
        //根据支付渠道获取具体的支付策略
        PayStrategy payStrategy = this.payStrategyMap.get(channel);
        if (payStrategy == null) {
            throw new UnsupportedOperationException("暂不支持的支付方式");
        }
        //调用支付策略进行支付
        return payStrategy.pay(orderNo);
    }
}
