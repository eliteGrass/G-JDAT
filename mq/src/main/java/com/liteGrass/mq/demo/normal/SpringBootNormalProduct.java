package com.liteGrass.mq.demo.normal;


import cn.hutool.core.date.DateUtil;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 普通消息
 * @Author liteGrass
 * @Date 2024/12/5 20:00
 */
@Service
public class SpringBootNormalProduct {

    @Value("${rocketmq.normal.topic.flowTopic}")
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

        })
        // 处理业务逻辑
        return true;
    }
}
