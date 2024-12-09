package com.liteGrass.mq.demo.trans;


import org.apache.rocketmq.client.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.apache.rocketmq.client.core.RocketMQTransactionChecker;

import java.util.Map;

/**
 * @Description 事务监听
 * @Author liteGrass
 * @Date 2024/12/9 18:05
 */
@RocketMQTransactionListener
public class SpringBootTransListener implements RocketMQTransactionChecker {

    @Override
    public TransactionResolution check(MessageView messageView) {
        Map<String, String> properties = messageView.getProperties();

        return null;
    }

}
