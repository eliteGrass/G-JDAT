package com.liteGrass.mq.demo.trans;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.common.Pair;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @Description 事务消息
 * @Author liteGrass
 * @Date 2024/12/9 16:04
 */
@Service
public class SpringBootTransProduct {

    @Value("${config.normal.topic.transTopic}")
    private String transTopic;

    @Resource
    private RocketMQClientTemplate rocketMQClientTemplate;

    public boolean syncSendTransMessage() throws ClientException {
        // 发送事务消息
        Pair<SendReceipt, Transaction> transactionPair = rocketMQClientTemplate
                .sendMessageInTransaction(transTopic, MessageBuilder.withPayload("test_message" + DateUtil.now())
                        .setHeader("transId", "123456")
                        .build());
        SendReceipt sendReceipt = transactionPair.getSendReceipt();
        MessageId messageId = sendReceipt.getMessageId();
        System.out.println(messageId);
        // 事务相关消息,进行业务代码相关执行
        System.out.println("执行业务代码");
        return true;
    }
}
