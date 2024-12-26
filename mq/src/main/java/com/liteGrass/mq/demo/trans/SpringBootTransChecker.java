package com.liteGrass.mq.demo.trans;


import cn.hutool.core.util.StrUtil;
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
public class SpringBootTransChecker implements RocketMQTransactionChecker {

    @Override
    public TransactionResolution check(MessageView messageView) {
        Map<String, String> properties = messageView.getProperties();
        // 获取相关属性id进行检查，返回状态码
        String transId = properties.get("transId");
        // 根据业务id进行回差
        System.out.println(transId);
        return TransactionResolution.COMMIT;
    }

}
