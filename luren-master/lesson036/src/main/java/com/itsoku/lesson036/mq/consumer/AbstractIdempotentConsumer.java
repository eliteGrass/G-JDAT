package com.itsoku.lesson036.mq.consumer;

import com.itsoku.lesson036.mq.dto.Msg;
import com.itsoku.lesson036.mq.po.MsgConsumePO;
import com.itsoku.lesson036.mq.service.IdempotentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 幂等消费者，子类集成该类，便拥有了幂等消费 && 消费失败衰减式重试的能力
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 15:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public abstract class AbstractIdempotentConsumer<B, M extends Msg<B>> extends AbstractRetryConsumer<B, M> {
    @Autowired
    protected IdempotentService idempotentService;

    public void dispose(Message message) {
        super.dispose(message);
    }

    protected void consume(Message message, M msg, MsgConsumePO msgConsumerPO) {
        /**
         * 获取幂等的key，由(producer、producerBusId、consumerClassName) 3个字段组合成的
         * 生产者生成的同一条消息(producer && producerBusId 这2个值一样的，表示是同一条消息)，则这个消息只会被同一个消费者消费一次
         */
        String idempotentKey = this.getIdempotentKey(msgConsumerPO);
        //调用幂等工具类确保消息只会被成功消费一次，这里可以确保同样的idempotentKey，即使发生并发，下面方法第二个参数中的逻辑只会被成功处理一次
        this.idempotentService.idempotent(idempotentKey, () -> {
            //调用子类的方法，消费消息
            this.idempotentConsume(msg.getBody());
        });
    }

    /**
     * 幂等key
     *
     * @param msgConsumerPO
     * @return
     */
    private String getIdempotentKey(MsgConsumePO msgConsumerPO) {
        return String.format("producer:%s,producerBusId:%s,consumerClassName:%s", msgConsumerPO.getProducer(), msgConsumerPO.getProducerBusId(), msgConsumerPO.getConsumerClassName());
    }

    /**
     * 需要幂等处理的逻辑，由子类实现
     *
     * @param body
     */
    protected abstract void idempotentConsume(B body);

}
