package com.liteGrass.mq.demo.delay;


import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Description 消费者
 * @Author liteGrass
 * @Date 2024/12/9 14:13
 */
@Service
@RocketMQMessageListener(consumerGroup = "spring_boot_delay_consumer", topic = "${config.normal.topic.delayTopic}", tag = "*")
public class SpringBootDelayConsumer implements RocketMQListener {

    @Resource
    private RocketMQClientTemplate rocketMQClientTemplate;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        ByteBuffer byteBuffer = messageView.getBody();
        System.out.println(StrUtil.str(byteBuffer, StandardCharsets.UTF_8) + System.currentTimeMillis());
        return ConsumeResult.SUCCESS;
    }
}
