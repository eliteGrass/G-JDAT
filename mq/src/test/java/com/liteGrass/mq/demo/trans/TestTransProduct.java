package com.liteGrass.mq.demo.trans;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.liteGrass.mq.tools.config.BaseMqConfig;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Description 事务生产者
 * @Author liteGrass
 * @Date 2024/12/5 15:59
 */
public class TestTransProduct {

    public static final String TOPIC = "test_trans_topic";
    public static final String TAG = "test_trans";
    public static final String CONSUMER_GROUP = "test_trans_consumer_group";


    @Test
    public void testMethod1() throws Exception {
        // 生产数事务消息
        Producer producer = BaseMqConfig.getClientServiceProvider().newProducerBuilder()
                .setTopics(TOPIC)
                .setClientConfiguration(BaseMqConfig.getClientConfiguration())
                .setTransactionChecker(messageView -> {
                    // 事务检查器, 当事务消息状态反馈超时的时候进行回查，默认6秒后进行回查
                    System.out.println("进行事务回查机制");
                    String orderId = messageView.getProperties().get("OrderId");
                    if (StrUtil.equals(orderId, "ok")) {
                        System.out.println("事务回查---提交");
                        return TransactionResolution.COMMIT;
                    }
                    System.out.println("事务回查---回滚");
                    return TransactionResolution.ROLLBACK;
                })
                .build();

        Transaction transaction = producer.beginTransaction();
        Message message = new MessageBuilderImpl()
                .setTopic(TOPIC)
                .setTag(TAG)
                .setKeys(TOPIC + DateUtil.now())
                .addProperty("OrderId", "ok")
                .setBody(StrUtil.bytes("test_message" + DateUtil.now()))
                .build();
        SendReceipt sendReceipt = producer.send(message, transaction);
        System.out.println(sendReceipt.getMessageId());

        // 执行本地事务
        System.out.println("业务代码");
        TimeUnit.SECONDS.sleep(10);
//        System.out.println("事务提交");
//        transaction.commit();


        Thread.sleep(Long.MAX_VALUE);
        producer.close();
    }

}
