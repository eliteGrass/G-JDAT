package com.liteGrass.mq.demo.order;

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
 *
 * @Description 顺序消息生产者
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2024/12/3 14:13
 */
public class TestOrderProduct {

    public static final String TOPIC = "test_order_topic";
    public static final String TAG = "test_order";
    public static final String CONSUMER_GROUP = "test_order_consumer_group";
    public static final String MESSAGE_GROUP = "test_order_message_group";

    /**
     * - 消息生产的顺序性：
     *      - 相同topic中
     *      - 同一个消息组的消息会进入同一个消息队列
     */
    @Test
    public void testMethod1() throws ClientException, IOException {
        // 顺序消息
        Producer producer = BaseMqConfig.getClientServiceProvider().newProducerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setTopics(TestOrderProduct.TOPIC)
                .build();

        // 相同的key怎么进行判断相关消息
        Message message = new MessageBuilderImpl()
                .setTag(TestOrderProduct.TAG)
                .setKeys(TestOrderProduct.TOPIC + DateUtil.now())
                .setBody(StrUtil.bytes("test_message" + DateUtil.now()))
                .setMessageGroup(TestOrderProduct.MESSAGE_GROUP)
                .build();

        SendReceipt sendReceipt = producer.send(message);
        System.out.println(sendReceipt.getMessageId());
        producer.close();
    }

    @Test
    public void testMethod2() {

    }

}
