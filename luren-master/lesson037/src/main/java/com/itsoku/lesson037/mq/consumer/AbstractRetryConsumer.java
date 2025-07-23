package com.itsoku.lesson037.mq.consumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson037.lock.DistributeLock;
import com.itsoku.lesson037.mq.consumer.retry.MsgConsumeRetry;
import com.itsoku.lesson037.mq.consumer.retry.MsgConsumeRetryResult;
import com.itsoku.lesson037.mq.dto.Msg;
import com.itsoku.lesson037.mq.enums.MsgConsumerStatusEnum;
import com.itsoku.lesson037.mq.po.MsgConsumePO;
import com.itsoku.lesson037.mq.sender.IMsgSender;
import com.itsoku.lesson037.mq.service.IMsgConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 幂等消费者，子类集成该类，便拥有了幂等消费 && 消费失败衰减式重试的能力
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 15:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public abstract class AbstractRetryConsumer<B, M extends Msg<B>> {
    public static final String CHARSET_NAME = "UTF-8";
    @Autowired
    protected IMsgSender msgSender;
    @Autowired
    protected MsgConsumeRetry msgConsumeRetry;
    @Autowired
    protected IMsgConsumerService msgConsumerService;
    @Autowired
    protected TransactionTemplate transactionTemplate;
    @Autowired
    protected DistributeLock distributeLock;
    protected Type msgType;

    public AbstractRetryConsumer() {
        this.msgType = this.getMsgType();
    }

    public void dispose(Message message) {
        //从message中拿到消息
        M msg = this.getMsg(message);
        //创建消息和消费者关联记录
        MsgConsumePO msgConsumerPO = createMsgConsumePO(message, msg);
        //根据消费记录（MsgConsumePO）判断，此消息是否还需要消费？若不需要消费了，则直接返回
        if (!hasConsume(msgConsumerPO)) {
            return;
        }
        try {
            //消费消息
            this.consume(message, msg, msgConsumerPO);
            //消费成功，记录日志
            this.consumeSuccess(msgConsumerPO);
        } catch (Exception exception) {
            log.error("消息消费异常:{}", exception.getMessage());
            //消费失败，记录日志 && 重试（若需要重试则会进行重试）
            this.consumeFail(message, msg, msgConsumerPO, exception);
        }
    }

    /**
     * 创建消息和消费者关联记录，对于这3个值(producer、producerBusId、consumerClassName)一样的，只会产生一条记录
     *
     * @param message
     * @param msg
     * @return
     */
    private MsgConsumePO createMsgConsumePO(Message message, M msg) {
        String queueName = getQueueName(message);
        String consumerClassName = getConsumerClassName();
        MsgConsumePO msgConsumerPO = this.msgConsumerService.getAndCreate(msg, consumerClassName, queueName);
        return msgConsumerPO;
    }

    /**
     * 消费失败
     *
     * @param message
     * @param msg
     * @param msgConsumerPO
     * @param exception
     */
    private void consumeFail(Message message, M msg, MsgConsumePO msgConsumerPO, Exception exception) {
        //获取消费重试信息
        MsgConsumeRetryResult retryResult = this.msgConsumeRetry.getRetryResult(msgConsumerPO);
        //异常信息
        String failMsg = ExceptionUtil.getRootCauseMessage(exception);
        //是否需要重试？
        boolean retry = retryResult.isRetry();
        //下次重试时间
        LocalDateTime nextRetryTime = retryResult.getNextRetryTime();
        //将消息状态置为失败（且记录失败信息、是否需要重试，下次重试时间）
        this.msgConsumerService.updateStatusFail(msgConsumerPO.getId(), msgConsumerPO.getStatus(), failMsg, retry, nextRetryTime);

        if (retry) {
            //消费失败，需要重试的，投递重试消息继续重试
            String queueName = this.getQueueName(message);
            long delayTimeMills = ChronoUnit.MILLIS.between(LocalDateTime.now(), nextRetryTime);
            //消费失败，采用延迟的方式，将消息再次投递到队列进行重试（这里exchange为null，routingKey为队列名称时，会直接减消息投递到队列）
            this.msgSender.send(null, queueName, delayTimeMills, TimeUnit.MILLISECONDS, msg);
        } else {
            //如果走到这里，还是失败，且不需要重试了，建议告警，说明重试次数达到上限了，请求人工干预
            //todo 请执行告警代码，人工干预
        }
    }

    private void consumeSuccess(MsgConsumePO msgConsumerPO) {
        this.msgConsumerService.updateStatusSuccess(msgConsumerPO.getId(), msgConsumerPO.getStatus());
    }

    /**
     * 消费者类名
     *
     * @return
     */
    protected String getConsumerClassName() {
        return this.getClass().getName();
    }


    protected M getMsg(Message message) {
        try {
            return JSONUtil.toBean(new String(message.getBody(), CHARSET_NAME), this.msgType, true);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取消息
     *
     * @param msgJson
     * @return
     */
    protected M getMsg(String msgJson) {
        return JSONUtil.toBean(msgJson, this.msgType, true);
    }

    /**
     * 解析得到消息的类型，也就是 AbstractIdempotentConsumer<B, M extends Msg<B>> 第二个泛型参数的类型
     *
     * @return
     */
    protected Type getMsgType() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type msgType = parameterizedType.getActualTypeArguments()[1];
        return msgType;
    }

    /**
     * 判断消息是否需要消费？
     *
     * @param msgConsumerPO
     * @return
     */
    protected boolean hasConsume(MsgConsumePO msgConsumerPO) {
        // 没有消费记录，说明是首次消费，需要消费
        if (msgConsumerPO == null) {
            return true;
        }
        // 消费中
        if (msgConsumerPO.getStatus().equals(MsgConsumerStatusEnum.INIT.getStatus())) {
            return true;
        }
        // 消费失败了 && 需要重试
        if (Objects.equals(msgConsumerPO.getStatus(), MsgConsumerStatusEnum.FAIL.getStatus()) &&
                Objects.equals(msgConsumerPO.getConsumeRetry(), 1)) {
            return true;
        }
        return false;
    }


    /**
     * 获取队列名称
     *
     * @param message
     * @return
     */
    protected String getQueueName(Message message) {
        return message.getMessageProperties().getConsumerQueue();
    }

    /**
     * 消息处理逻辑，由子类实现
     *
     * @param message 原始消息
     * @param msg     消息体
     */
    protected abstract void consume(Message message, M msg, MsgConsumePO msgConsumerPO);
}
