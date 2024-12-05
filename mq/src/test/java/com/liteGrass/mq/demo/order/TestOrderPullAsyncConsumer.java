package com.liteGrass.mq.demo.order;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.liteGrass.juc.threadFactory.ThreadFactoryUtil;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description 异步的方式
 * @Author liteGrass
 * @Date 2024/12/5 13:20
 */
public class TestOrderPullAsyncConsumer {

    @Test
    public void testMethod1() throws ClientException {
        SimpleConsumer simpleConsumer = BaseMqConfig.getClientServiceProvider().newSimpleConsumerBuilder()
                .setConsumerGroup(TestOrderProduct.CONSUMER_GROUP)
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setSubscriptionExpressions(Collections.singletonMap(TestOrderProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setAwaitDuration(Duration.ofSeconds(30))
                .build();

        // 获取异步消息
        CompletableFuture<List<MessageView>> completableFuture = simpleConsumer.receiveAsync(10, Duration.ofSeconds(30));
        completableFuture.whenCompleteAsync((messages, exception) -> {
                    Assert.isNull(exception, () -> new RuntimeException("接收消息出现异常"));
                    // 接收消息
                    messages.forEach(messageView -> {
                        // 进行业务处理
                        System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                        System.out.println(messageView.getMessageGroup() + " --- ConsumeResult.SUCCESS");
                        // 异步确认,
                        simpleConsumer.ackAsync(messageView).whenCompleteAsync( (v, ex) -> {
                            // 确认继续处理，进行事务提交
                        });
                    });
            }, ThreadFactoryUtil.getVirtualThreadTaskExecutor())
            .exceptionally(ex -> {
                // 整体异常处理
                System.out.println("整体异常处理");
                return null;
            });
    }

}
