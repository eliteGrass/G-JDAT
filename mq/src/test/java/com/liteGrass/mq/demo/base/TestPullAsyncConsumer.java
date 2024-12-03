package com.liteGrass.mq.demo.base;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
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
import java.util.stream.Collectors;

/**
 *
 * @Description 异步方式
 * @Author liteGrass
 * @Date 2024/11/28 15:35
 */
public class TestPullAsyncConsumer {

    /**
     *
     * 异步进行消费：
     *          1、我们在异步接收后，可以通过两种方式进行确认，一种是同步确认，一种是异步确认
     *              - simpleConsumer.ack：同步确认
     *              - simpleConsumer.ackAsync：异步确认，返回CompletaleFuture，我们可以再此处理线程确认成功及失败后的逻辑
     *                      - 我们在确认成功或者失败后，一般不做补偿机制，因为我们有重试机制，我们在重试机制达到上限之后在进行相关的操作
     *                          也可以进行手动的获取重试次数
     *                              - msg.getReconsumeTimes()：获取消息的重试次数
     *
     *              - 在我们调用receiveAsync返回CompletableFuture我们已经开启线程进行业务操作，如果在性能循序范围能，我们是可以通过同步的方式进行调用的，如果
     *                性能要求比较高我们可以通过异步的方式进行消息确认
     *              - 无论同步还是异步的方式，我们在消息确认失败后，都会进行的重试工作。我们在代码也可进行相关的日志记录工作。
     *          2、CompletabeFuture的使用：whenCompleteAsync和whenComplete都是无论成功或者失败都会执行
     *             他们区别是在前者单独启动一个线程进行操作
     * @throws ClientException
     */
    @Test
    public void testMethod1() throws ClientException, InterruptedException {
        SimpleConsumer simpleConsumer = BaseMqConfig.getClientServiceProvider().newSimpleConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestBaseProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestBaseProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setAwaitDuration(Duration.ofSeconds(10)) // 没有消息的时候消费者的阻塞时间，等待消息的时间
                .build();
        do {
            CompletableFuture<List<MessageView>> completableFuture = simpleConsumer.receiveAsync(5, Duration.ofSeconds(10));
            // 异步编排执行, 内部会掉用相关处理方法，
            // 如果throw 一个ack或者业务异常就会全部失败
            completableFuture.whenCompleteAsync((messages, exception) -> {
                        // 处理相关异常并进行抛出
                        Assert.isNull(exception, () -> new RuntimeException("接收失败"));
                        // 循环进行处理
                        for (MessageView messageView : messages) {
                            // 对消息进行业务处理，如果出现异常，抛出业务一场
                            try {
                                System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                            } catch (Exception e) {
                                // 处理业务异常，进行日志记录
                                System.out.println("业务异常处理");
                            }
                            // 处理消息确认异常，只有该确认机制处理完成后，成能执行下一个处理逻辑
                            try {
                                simpleConsumer.ack(messageView);
                            } catch (ClientException e) {
                                // 处理消息确认异常
                                System.out.println("消息确认异常");
                            }
                        }
                    }, ThreadFactoryUtil.getVirtualThreadTaskExecutor())
                    .exceptionally(ex -> {
                        // 整体异常处理
                        ex.printStackTrace();
                        System.out.println("整体异常处理");
                        return null;
                    });
            Thread.sleep(Duration.ofSeconds(10));
        } while (true);

    }


    @Test
    public void testMethod2() throws ClientException, InterruptedException {
        SimpleConsumer simpleConsumer = BaseMqConfig.getClientServiceProvider().newSimpleConsumerBuilder()
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setConsumerGroup(TestBaseProduct.CONSUMER_GROUP)
                .setSubscriptionExpressions(Collections.singletonMap(TestBaseProduct.TOPIC, new FilterExpression("*", FilterExpressionType.TAG)))
                .setAwaitDuration(Duration.ofSeconds(10)) // 没有消息的时候消费者的阻塞时间，等待消息的时间
                .build();
        do {
            CompletableFuture<List<MessageView>> completableFuture = simpleConsumer.receiveAsync(10, Duration.ofSeconds(10));
            // 使用异步确认机制
            completableFuture.whenCompleteAsync((messages, exception) -> {
                        // 处理相关异常并进行抛出
                        Assert.isNull(exception, () -> new RuntimeException("接收失败"));
                        Map<MessageView, CompletableFuture<Void>> messageMap = messages.stream().collect(Collectors.toMap(message -> message, simpleConsumer::ackAsync));
                        // 开始进行业务处理，如果业务处理
                        messageMap.forEach((messageView, future) -> {
                            try {
                                // 业务进行相关处理
                                System.out.println(StrUtil.format("接收到消息，消息id为：{}，消息体为：{}", messageView.getMessageId(), StrUtil.str(messageView.getBody(), StandardCharsets.UTF_8)));
                            } catch (Exception e) {
                                // 进行业务异常处理
                                System.out.println("业务异常");
                                return;
                            }
                            // 进行消息确认相关操作
                            future.whenCompleteAsync( (msg, ex) -> {
                                // 进行消费失败回调操作
                                if (ObjectUtil.isNotNull(ex)) {
                                    // 消费失败
                                    System.out.println("消费失败");
                                } else {
                                    // 消费成功回调
                                    System.out.println("消费成功");
                                }
                            }, ThreadFactoryUtil.getVirtualThreadTaskExecutor());
                        });
                    }, ThreadFactoryUtil.getVirtualThreadTaskExecutor())
                    .exceptionally(ex -> {
                        // 整体异常处理
                        System.out.println("整体异常处理");
                        return null;
                    });
            Thread.sleep(Duration.ofSeconds(10));
        } while (true);

    }
}
