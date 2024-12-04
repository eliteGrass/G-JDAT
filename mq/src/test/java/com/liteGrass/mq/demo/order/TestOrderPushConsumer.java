package com.liteGrass.mq.demo.order;


import cn.hutool.core.util.StrUtil;
import com.liteGrass.mq.tools.config.BaseMqConfig;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

/**
 * @Description 顺序消费者
 * @Author liteGrass
 * @Date 2024/12/4 14:45
 */
public class TestOrderPushConsumer {

    @Test
    public void testMethod1() throws ClientException, InterruptedException, IOException {
        PushConsumer pushConsumer = BaseMqConfig.getClientServiceProvider().newPushConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestOrderProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestOrderProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setMessageListener(messageView -> {
                    System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return ConsumeResult.SUCCESS;
                })
                .build();
        Thread.sleep(Long.MAX_VALUE);
        pushConsumer.close();
    }

}
