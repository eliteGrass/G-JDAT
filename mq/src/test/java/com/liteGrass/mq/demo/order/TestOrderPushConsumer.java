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
import java.util.Collections;
import java.util.Map;

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
                .setSubscriptionExpressions(Collections.singletonMap("test_trans_topic", new FilterExpression("*", FilterExpressionType.TAG)))
                .setMessageListener(messageView -> {
                    System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                    /*try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }*/
                    System.out.println(messageView.getMessageGroup() + " --- ConsumeResult.SUCCESS");
                    Map<String, String> properties = messageView.getProperties();
                    return ConsumeResult.SUCCESS;
                })
                .setConsumptionThreadCount(10)  //设置线程数量
                .build();
        Thread.sleep(Long.MAX_VALUE);
        pushConsumer.close();
    }

}
