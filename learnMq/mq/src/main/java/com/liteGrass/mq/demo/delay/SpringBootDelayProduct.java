package com.liteGrass.mq.demo.delay;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @Description 延迟消息
 * @Author liteGrass
 * @Date 2024/12/9 14:08
 */
@Service
public class SpringBootDelayProduct {

    @Value("${config.normal.topic.delayTopic}")
    private String delayTopic;

    @Resource
    private RocketMQClientTemplate rocketMQClientTemplate;

    public boolean syncSendDelayMessage() {
        // 延时消息
        SendReceipt sendReceipt = rocketMQClientTemplate.syncSendDelayMessage(delayTopic, "test_message" + DateUtil.now(), Duration.ofSeconds(60));
        System.out.println(sendReceipt.getMessageId());
        return true;
    }

}
