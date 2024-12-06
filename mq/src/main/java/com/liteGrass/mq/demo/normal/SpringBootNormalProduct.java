package com.liteGrass.mq.demo.normal;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @Description 普通消息
 * @Author liteGrass
 * @Date 2024/12/5 20:00
 */
@Service
public class SpringBootNormalProduct {

    @Value("${config.normal.topic.flowTopic}")
    private String normalTopic;

    @Resource
    private RocketMQClientTemplate rocketMQClientTemplate;

    /**
     *
     * 异步发送消息到mq
     * @return
     */
    public boolean asyncSendNormalMessage() {
        CompletableFuture<SendReceipt> sendCompletableFuture = rocketMQClientTemplate.asyncSendNormalMessage(normalTopic, "test_message" + DateUtil.now(), new CompletableFuture<>());
        // 如果消息发送失败，进行日志记录功能，重新进行发送
        sendCompletableFuture.whenCompleteAsync((res, ex) -> {
            // 如果存在异常，进行补偿处理
            if (ObjectUtil.isNotNull(ex)) {
                System.out.printf("出现异常{}，进行补偿机制", ex.getMessage());
            } else {
                System.out.println("发送成功");
            }
        });
        // 处理业务逻辑, 发送消息成功
        return true;
    }

    public boolean syncSendNormalMessage() {
        for (int i = 0; i < 100; i++) {
            SendReceipt sendReceipt = rocketMQClientTemplate.syncSendNormalMessage(normalTopic, i + "test_message" + DateUtil.now());
            System.out.println(sendReceipt.getMessageId());
        }
        return true;
    }

}
