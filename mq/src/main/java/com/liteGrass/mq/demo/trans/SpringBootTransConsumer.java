package com.liteGrass.mq.demo.trans;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * @Description 消费者
 * @Author liteGrass
 * @Date 2024/12/9 18:00
 */
@Service
public class SpringBootTransConsumer implements RocketMQListener {

    @Override
    public ConsumeResult consume(MessageView messageView) {
        Map<String, String> properties = messageView.getProperties();
        String transId = properties.get("transId");
        //获取消息体
        ByteBuffer byteBuffer = messageView.getBody();
        System.out.println(StrUtil.str(byteBuffer, StandardCharsets.UTF_8) + System.currentTimeMillis() + "---" + transId);
        return ConsumeResult.SUCCESS;
    }

}
