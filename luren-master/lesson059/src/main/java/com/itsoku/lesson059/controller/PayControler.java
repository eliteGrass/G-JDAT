package com.itsoku.lesson059.controller;

import com.itsoku.lesson059.service.PayService;
import com.itsoku.lesson059.service.NewPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/4 20:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class PayControler {
    @Autowired
    private PayService payService;

    /**
     * 订单支付，根据不同的支付渠道，走不同的支付逻辑
     *
     * @param channel 支付渠道
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("/pay")
    public String pay(@RequestParam("channel") String channel, @RequestParam("orderNo") String orderNo) {
        return this.payService.pay(channel, orderNo);
    }


    @Autowired
    private NewPayService newPayService;

    /**
     * 支付
     *
     * @param channel 支付渠道
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("/newPay")
    public String newPay(@RequestParam("channel") String channel, @RequestParam("orderNo") String orderNo) {
        return this.newPayService.pay(channel, orderNo);
    }
}
