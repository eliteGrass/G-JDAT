package com.liteGrass.mq.demo.normal;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.validation.Valid;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Description 消费者
 * @Author liteGrass
 * @Date 2024/12/6 11:20
 */
@Service
@RocketMQMessageListener(consumerGroup = "spring_boot_normal_consumer", topic = "${config.normal.topic.flowTopic}", tag = "*")
public class SpringBootNormalConsumer implements RocketMQListener {

    @Value("${server.port}")
    private int port;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        if (ObjectUtil.equal(port, 8089)) {
            try {
                Thread.sleep(Duration.ofSeconds(3));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //获取消息体
        ByteBuffer byteBuffer = messageView.getBody();
        System.out.println(StrUtil.str(byteBuffer, StandardCharsets.UTF_8) + System.currentTimeMillis());
        return ConsumeResult.SUCCESS;
    }
}
