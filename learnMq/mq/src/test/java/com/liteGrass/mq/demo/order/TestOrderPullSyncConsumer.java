package com.liteGrass.mq.demo.order;


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
import java.util.concurrent.TimeUnit;

/**
 * @Description 同步拉取消费
 * @Author liteGrass
 * @Date 2024/12/5 10:03
 */
public class TestOrderPullSyncConsumer {

    @Test
    public void testMethod1() throws ClientException {
        SimpleConsumer simpleConsumer = BaseMqConfig.getClientServiceProvider().newSimpleConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestOrderProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestOrderProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setAwaitDuration(Duration.ofSeconds(10)) // 没有消息的时候间隔时间
                .build();

        do {
            // 批量同步处理, 每次批量处理的不是一个队列内的消息，只要有一个队列内的消息ack后才能再次进行调用
            List<MessageView> messageViews = simpleConsumer.receive(10, Duration.ofSeconds(30));    // 第二个参数为消息不可见时间
            messageViews.forEach(messageView -> {
                // 进行消息的处理工作
                System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                System.out.println(messageView.getMessageGroup() + " --- ConsumeResult.SUCCESS");
                try {
                    simpleConsumer.ack(messageView);
                } catch (ClientException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("-----------------------------------");
        } while (true);
    }

}
