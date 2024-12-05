package com.liteGrass.mq.demo.delay;


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

/**
 * @Description 延时、定时消息
 * @Author liteGrass
 * @Date 2024/12/5 15:17
 */
public class TestDelayProduct {

    public static final String TOPIC = "test_delay_topic";
    public static final String TAG = "test_delay";
    public static final String CONSUMER_GROUP = "test_delay_consumer_group";
    public static final String MESSAGE_GROUP = "test_delay_message_group";

    @Test
    public void testMethod1() throws ClientException, IOException {
        // 初始化一个生产者并绑定相关的配置
        Producer producer = BaseMqConfig.getClientServiceProvider().newProducerBuilder()
                .setTopics(TOPIC)
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .build();
        Long deliverTimeStamp = System.currentTimeMillis() + 1L * 60 * 1000;
        Message message = new MessageBuilderImpl().setTopic(TOPIC)
                .setKeys(TOPIC + DateUtil.now())
                .setTag(TAG)
                .setBody(StrUtil.bytes("test_message" + DateUtil.now()))
                .setDeliveryTimestamp(deliverTimeStamp)   // 设置延时/定时消息执行时间
                .build();

        SendReceipt sendReceipt = producer.send(message);
        System.out.println(sendReceipt.getMessageId());
        producer.close();
    }

}
