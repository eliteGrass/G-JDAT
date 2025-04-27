package com.liteGrass.demo;


/**
 * Copyright (c) 2024 Huahui Information Technology Co., LTD.
 * and China Nuclear Engineering & Construction Corporation Limited (Loongxin Authors).
 * All Rights Reserved.
 * <p>
 * This software is part of the Zhonghe Loongxin Development Platform (the "Platform").
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <p>
 * For more information about the Platform, terms and conditions, and user licenses,
 * please visit our official website at www.icnecc.com.cn or contact us directly.
 */

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.Validators;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.common.protocol.NamespaceUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName RocketMqConfig
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 0:14
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnClass(RocketMQTemplate.class)
public class ZhlxRocketMqProduct {

    private final RocketMQTemplate rocketMQTemplate;
    private static final Method TO_MESSAGE_METHOD = ReflectUtil.getMethod(RocketMQTemplate.class, "createRocketMqMessage", String.class, org.springframework.messaging.Message.class);


    /**
     * 进行消息的发送机制
     *
     * @param topic
     * @param message
     * @param transId
     * @param queueHashKey
     * @return
     * @throws MQClientException
     */
    public SendResult sendMsgTransactionAndOrder(String topic, org.springframework.messaging.Message<?> message, Object transId, String queueHashKey) throws MQClientException {
        DefaultMQProducer producer = rocketMQTemplate.getProducer();
        Message msg = ReflectUtil.invoke(rocketMQTemplate, TO_MESSAGE_METHOD, topic, message);
        NamespaceUtil.wrapNamespace(producer.getNamespace(), topic);
        TransactionMQProducer transactionMQProducer = (TransactionMQProducer) producer;
        return this.doSendMsgTransactionAndOrder(transactionMQProducer, msg, transactionMQProducer.getTransactionListener(),
                transId, rocketMQTemplate.getMessageQueueSelector(), queueHashKey, producer.getSendMsgTimeout());
    }

    public SendResult sendMsgTransactionAndOrder(String topic, org.springframework.messaging.Message<?> message, Object transId, String queueHashKey,
                                                 TransactionListener transactionListener) throws MQClientException {
        DefaultMQProducer producer = rocketMQTemplate.getProducer();
        Message msg = ReflectUtil.invoke(rocketMQTemplate, TO_MESSAGE_METHOD, topic, message);
        NamespaceUtil.wrapNamespace(producer.getNamespace(), topic);
        TransactionMQProducer transactionMQProducer = (TransactionMQProducer) producer;
        return this.doSendMsgTransactionAndOrder(transactionMQProducer, msg, transactionListener,
                transId, rocketMQTemplate.getMessageQueueSelector(), queueHashKey, producer.getSendMsgTimeout());
    }

    public SendResult sendMsgTransactionAndOrder(String topic, org.springframework.messaging.Message<?> message, Object transId, String queueHashKey,
                                                 TransactionListener transactionListener, MessageQueueSelector messageQueueSelector) throws MQClientException {
        DefaultMQProducer producer = rocketMQTemplate.getProducer();
        Message msg = ReflectUtil.invoke(rocketMQTemplate, TO_MESSAGE_METHOD, topic, message);
        NamespaceUtil.wrapNamespace(producer.getNamespace(), topic);
        TransactionMQProducer transactionMQProducer = (TransactionMQProducer) producer;
        return this.doSendMsgTransactionAndOrder(transactionMQProducer, msg, transactionListener,
                transId, rocketMQTemplate.getMessageQueueSelector(), queueHashKey, producer.getSendMsgTimeout());
    }

    public TransactionSendResult doSendMsgTransactionAndOrder(TransactionMQProducer producer, Message msg,
                                                          TransactionListener transactionListener, Object arg,
                                                          MessageQueueSelector messageQueueSelector, String hashKey, long timeout) throws MQClientException {
        DefaultMQProducerImpl defaultMQProducerImpl = producer.getDefaultMQProducerImpl();
        // 获取事务检查
        if (ObjectUtil.isNull(transactionListener)) {
            transactionListener = producer.getDefaultMQProducerImpl().getCheckListener();
        }
        // 检测消息分发器
        if (ObjectUtil.isNull(messageQueueSelector)) {
            messageQueueSelector = new SelectMessageQueueByHash();
        }
        if (msg.getDelayTimeLevel() != 0) {
            MessageAccessor.clearProperty(msg, "DELAY");
        }
        Validators.checkMessage(msg, producer);
        SendResult sendResult = null;
        MessageAccessor.putProperty(msg, "TRAN_MSG", "true");
        MessageAccessor.putProperty(msg, "PGROUP", producer.getProducerGroup());
        try {
            sendResult = defaultMQProducerImpl.send(msg, messageQueueSelector, hashKey, timeout);
        } catch (Exception e) {
            throw new MQClientException("send message Exception", e);
        }
        LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
        Throwable localException = null;
        switch (sendResult.getSendStatus()) {
            case SEND_OK:
                try {
                    if (sendResult.getTransactionId() != null) {
                        msg.putUserProperty("__transactionId__", sendResult.getTransactionId());
                    }

                    String transactionId = msg.getProperty("UNIQ_KEY");
                    if (null != transactionId && !"".equals(transactionId)) {
                        msg.setTransactionId(transactionId);
                    }

                    localTransactionState = transactionListener.executeLocalTransaction(msg, arg);

                    if (null == localTransactionState) {
                        localTransactionState = LocalTransactionState.UNKNOW;
                    }

                    if (localTransactionState != LocalTransactionState.COMMIT_MESSAGE) {
                    }
                } catch (Throwable e) {
                    localException = e;
                }
                break;
            case FLUSH_DISK_TIMEOUT:
            case FLUSH_SLAVE_TIMEOUT:
            case SLAVE_NOT_AVAILABLE:
                localTransactionState = LocalTransactionState.ROLLBACK_MESSAGE;
        }

        try {
            defaultMQProducerImpl.endTransaction(sendResult, localTransactionState, localException);
        } catch (Exception e) {
        }

        TransactionSendResult transactionSendResult = new TransactionSendResult();
        transactionSendResult.setSendStatus(sendResult.getSendStatus());
        transactionSendResult.setMessageQueue(sendResult.getMessageQueue());
        transactionSendResult.setMsgId(sendResult.getMsgId());
        transactionSendResult.setQueueOffset(sendResult.getQueueOffset());
        transactionSendResult.setTransactionId(sendResult.getTransactionId());
        transactionSendResult.setLocalTransactionState(localTransactionState);
        return transactionSendResult;
    }


}
