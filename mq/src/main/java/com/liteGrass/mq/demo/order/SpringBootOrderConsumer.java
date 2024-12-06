package com.liteGrass.mq.demo.order;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @Description 消费者
 * @Author liteGrass
 * @Date 2024/12/6 15:37
 */
@Service
@RocketMQMessageListener(topic = "${config.normal.topic.orderTopic}", consumerGroup = "spring_boot_order_consumer", tag = "*")
public class SpringBootOrderConsumer implements RocketMQListener {

    @Value("${server.port}")
    private int port;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        //获取消息体
        ByteBuffer byteBuffer = messageView.getBody();
        System.out.println(StrUtil.str(byteBuffer, StandardCharsets.UTF_8) + System.currentTimeMillis());
        return ConsumeResult.SUCCESS;
    }
}
