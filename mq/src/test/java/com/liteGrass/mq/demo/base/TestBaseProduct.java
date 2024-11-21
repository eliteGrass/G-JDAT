package com.liteGrass.mq.demo.base;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;

import java.io.IOException;

public class TestBaseProduct {

    public static void main(String[] args) throws ClientException {
        // 接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
        String endpoint = "123.56.51.189:8081";
        // 消息发送的目标Topic名称，需要提前创建。
        String topic = "test_base_topic";

        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration configuration = ClientConfiguration.newBuilder().setEndpoints(endpoint).build();
        // 初始化一个生产者并绑定相关的配置
        Producer producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();
        // 创建普通消息并进行发送
        MessageBuilderImpl messageBuilder = new MessageBuilderImpl();
        Message message = messageBuilder.setTopic(topic)
                //设置消息索引键，可根据关键字精确查找某条消息
                .setKeys("test_base_topic" + DateUtil.now())
                .setTag("test_base")
                .setBody(StrUtil.bytes("test_message" + DateUtil.now()))
                .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
            System.out.println(sendReceipt.getMessageId());
            producer.close();
        } catch (IOException e) {
            System.out.println("报错: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
