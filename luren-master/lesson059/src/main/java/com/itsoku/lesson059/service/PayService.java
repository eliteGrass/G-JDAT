package com.itsoku.lesson059.service;

import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/4 20:19 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class PayService {
    public String pay(String channel, String orderNo) {
        if ("wechat".equals(channel)) {
            //微信支付处理逻辑代码
            System.out.println("微信支付:" + orderNo);
        } else if ("alipay".equals(channel)) {
            //支付宝支付处理逻辑代码
            System.out.println("支付宝支付:" + orderNo);
        } else if ("bankcard".equals(channel)) {
            //银行卡支付处理逻辑代码
            System.out.println("银行卡支付:" + orderNo);
        }else {
            throw new UnsupportedOperationException("暂不支持的支付方式");
        }
        return "success";
    }
}
