package com.itsoku.lesson034.controller;

import cn.hutool.core.util.IdUtil;
import com.itsoku.lesson034.common.Result;
import com.itsoku.lesson034.common.ResultUtils;
import com.itsoku.lesson034.config.RabbitMQConfiguration;
import com.itsoku.lesson034.dto.OrderMsg;
import com.itsoku.lesson034.mq.sender.IMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {

    @Autowired
    private IMsgSender msgSender;

    /**
     * 发送顺序消息
     *
     * @return
     */
    @PostMapping("/sendSequential")
    public Result<Void> sendSequential() {
        String orderId = IdUtil.fastSimpleUUID();

        List<String> list = Arrays.asList("订单创建消息",
                "订单支付消息",
                "订单已发货",
                "买家确认收货",
                "订单已完成");
        for (String type : list) {
            msgSender.sendSequentialWithBody(orderId,
                    RabbitMQConfiguration.Order.EXCHANGE,
                    RabbitMQConfiguration.Order.ROUTING_KEY,
                    OrderMsg.builder().orderId(orderId).type(type).build());

        }
        return ResultUtils.success();
    }


}
