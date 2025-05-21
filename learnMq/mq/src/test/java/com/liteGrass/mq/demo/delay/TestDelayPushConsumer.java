package com.liteGrass.mq.demo.delay;


import cn.hutool.core.util.StrUtil;
import com.liteGrass.mq.demo.base.TestBaseProduct;
import com.liteGrass.mq.tools.config.BaseMqConfig;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

/**
 * @Description 延时消息消费
 * @Author liteGrass
 * @Date 2024/12/5 15:26
 */
public class TestDelayPushConsumer {

    @Test
    public void testMethod1() throws Exception {
        PushConsumer pushConsumer = BaseMqConfig.getClientServiceProvider().newPushConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestDelayProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestDelayProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setMessageListener(messageView -> {
                    System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                    try {
                        Thread.sleep(Duration.ofSeconds(5));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(messageView.getMessageGroup() + " --- ConsumeResult.SUCCESS");
                    return ConsumeResult.SUCCESS;
                })
                .build();
        Thread.sleep(Long.MAX_VALUE);
        pushConsumer.close();
    }

}
