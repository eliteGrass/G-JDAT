package com.itsoku.lesson059.pay;

import org.springframework.stereotype.Component;

/**
 * 信用卡支付
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/4 20:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class CreditcardPayStrategy implements PayStrategy {

    @Override
    public String payChannel() {
        return "creditcard";
    }

    @Override
    public String pay(String orderNo) {
        System.out.println("信用卡支付：" + orderNo);
        return "success";
    }
}
