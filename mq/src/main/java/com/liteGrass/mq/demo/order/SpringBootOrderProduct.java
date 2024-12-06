package com.liteGrass.mq.demo.order;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description 顺序消息
 * @Author liteGrass
 * @Date 2024/12/6 15:05
 */
@Service
public class SpringBootOrderProduct {

    @Value("${config.normal.topic.orderTopic}")
    private String orderTopic;

    @Resource
    private RocketMQClientTemplate rocketMQClientTemplate;

    public boolean syncSendOrderMessage() {
        for (int i = 0; i < 3000; i++) {
            SendReceipt sendReceipt = rocketMQClientTemplate.syncSendFifoMessage(orderTopic, i + "test_message" + DateUtil.now(), "order-" + i % 8);
            System.out.println(sendReceipt.getMessageId());
        }
        return true;
    }

}
