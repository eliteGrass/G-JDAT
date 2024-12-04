package com.liteGrass.mq.demo.base;


import cn.hutool.core.util.StrUtil;
import com.liteGrass.mq.tools.config.BaseMqConfig;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;


/**
 *
 * @Description 同步方式
 * @Author liteGrass
 * @Date 2024/11/28 15:39
 */
public class TestBasePullSyncConsumer {

    @Test
    public void testMethod1() throws ClientException, InterruptedException {
        SimpleConsumer simpleConsumer = BaseMqConfig.getClientServiceProvider().newSimpleConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestBaseProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestBaseProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setAwaitDuration(Duration.ofSeconds(10)) // 没有消息的时候消费者的阻塞时间，等待消息的时间
                .build();
        // 内部进行循环进行消费
        do {
            // 1、消费的数量； 2、消息的不可见时间，在此期间该消息是不可见的对于别的消费者，防止重复消费。
            List<MessageView> messageViewList = simpleConsumer.receive(10, Duration.ofSeconds(30)); // 如果该步骤没有消息他会再次等待配置的等待时间
            messageViewList.forEach(messageView -> {
                try {
                    System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                    // 消费成共返回相应的ack标识，如果不返回就标识这消费失败，会进行重复消费工作
                    simpleConsumer.ack(messageView);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Thread.sleep(Duration.ofSeconds(10));
        } while (true);
    }

}
