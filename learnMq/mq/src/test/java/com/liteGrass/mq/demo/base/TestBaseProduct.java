package com.liteGrass.mq.demo.base;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.liteGrass.mq.tools.config.BaseMqConfig;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestBaseProduct {

    public static final String TOPIC = "test_base_topic";
    public static final String TAG = "test_base";
    public static final String CONSUMER_GROUP = "test_base_consumer_group";


    @Test
    public void testMethod1() throws ClientException {
        // 初始化一个生产者并绑定相关的配置
        Producer producer = BaseMqConfig.getClientServiceProvider().newProducerBuilder()
                .setTopics(TOPIC)
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .build();
        // 创建普通消息并进行发送
        MessageBuilderImpl messageBuilder = new MessageBuilderImpl();
        Message message = messageBuilder.setTopic(TOPIC)
                //设置消息索引键，可根据关键字精确查找某条消息
                .setKeys("test_base_topic" + DateUtil.now())
                .setTag(TAG)
                .setBody(StrUtil.bytes("test_message" + DateUtil.now()))
                .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
            System.out.println(sendReceipt.getMessageId());
            producer.close();
        } catch (Exception e) {
            System.out.println("报错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 消息进行批量发送
     *
     * 批量进行消息发送，普通消息。普通消息是无序的，无论我们怎么进行发送
     * 消息是以广播的形式进行分发的
     */
    @Test
    public void testMethod2() throws ClientException, IOException {
        // 初始化一个生产者并绑定相关的配置
        Producer producer = BaseMqConfig.getClientServiceProvider().newProducerBuilder()
                .setTopics(TOPIC)
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .build();
        // 创建普通消息并进行发送
        MessageBuilderImpl messageBuilder = new MessageBuilderImpl();
        for (int i = 0; i < 100; i++) {
            Message message = messageBuilder.setTopic(TOPIC)
                    //设置消息索引键，可根据关键字精确查找某条消息
                    .setKeys("test_base_topic")
                    .setTag(TAG)
                    .setBody(StrUtil.bytes(i + "test_message" + DateUtil.now()))
                    .build();
            try {
                SendReceipt sendReceipt = producer.send(message);
                System.out.println(sendReceipt.getMessageId());
            } catch (Exception e) {
                // 发送失败，进行处理
                System.out.println("报错: " + e.getMessage());
                e.printStackTrace();
            }
        }
        producer.close();
    }
}
