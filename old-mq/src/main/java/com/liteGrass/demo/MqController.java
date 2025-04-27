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

import com.liteGrass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.Validators;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MqController
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/23 23:03
 * @Description
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/mq")
public class MqController {

    private final RocketMQTemplate rocketMQTemplate;
    private final ZhlxRocketMqProduct zhlxRocketMqProduct;
    private final UserService userService;

//    @TransactionalMessage(topic = "test_trans_topic", tag = "*")
    @GetMapping("/save/{username}")
    public String saveUser(@PathVariable("username") String username) {
        userService.saveUser(username);
        return "success";
    }

    @TransactionalMessage(topic = "test_trans_topic", tag = "*")
    @GetMapping("/send/{queue}")
    public String sendNormalMsg(@PathVariable("queue") Long queue) throws MQClientException {
        /*rocketMQTemplate.setMessageQueueSelector(new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                return list.get(list.size()-1);
            }
        });*/
       /* DefaultMQProducer producer = rocketMQTemplate.getProducer();
        TransactionSendResult testTransTopic = rocketMQTemplate.sendMessageInTransaction("test_trans_topic", MessageBuilder.withPayload("111").build(), "1");
//        rocketMQTemplate.syncSendOrderly()
        rocketMQTemplate.syncSendOrderly("test_trans_topic", MessageBuilder.withPayload("222").build(), "1");
        Message msg = ReflectUtil.invoke(rocketMQTemplate, "createRocketMqMessage", "test_trans_topic", MessageBuilder.withPayload("111").build());
        this.sendMessageInTransaction((TransactionMQProducer)producer, msg, null, "1",
                rocketMQTemplate.getMessageQueueSelector(), "1", producer.getSendMsgTimeout());*/
        zhlxRocketMqProduct.sendMsgTransactionAndOrder("test_trans_topic", MessageBuilder.withPayload("111").build(),
                "123456", queue.toString());
        // int i = 1 / 0;
        System.out.println("方法开始执行");
        return "msg";
    }


    public TransactionSendResult sendMessageInTransaction(TransactionMQProducer producer, Message msg,
                                                          TransactionListener localTransactionListener, Object arg,
                                                          MessageQueueSelector messageQueueSelector, String hashKey, long timeout) throws MQClientException {
        DefaultMQProducerImpl defaultMQProducerImpl = producer.getDefaultMQProducerImpl();
        TransactionListener transactionListener = defaultMQProducerImpl.getCheckListener();
        if (null == localTransactionListener && null == transactionListener) {
            throw new MQClientException("tranExecutor is null", (Throwable)null);
        } else {
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

                        if (null != localTransactionListener) {
                            localTransactionState = localTransactionListener.executeLocalTransaction(msg, arg);
                        } else {
                            localTransactionState = transactionListener.executeLocalTransaction(msg, arg);
                        }

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

}
