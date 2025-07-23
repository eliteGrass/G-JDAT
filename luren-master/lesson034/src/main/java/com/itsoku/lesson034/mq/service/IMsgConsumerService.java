package com.itsoku.lesson034.mq.service;

import com.itsoku.lesson034.common.service.IBaseService;
import com.itsoku.lesson034.mq.dto.Msg;
import com.itsoku.lesson034.mq.po.MsgConsumePO;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:06 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IMsgConsumerService extends IBaseService<MsgConsumePO> {

    /**
     * 获取消息的消费记录，记录不存在的时候则创建一条记录
     *
     * @param msg       消息
     * @param consumerClassName  消费者类名
     * @param queueName rabbitmq 队列名称
     * @return
     */
    MsgConsumePO getAndCreate(Msg<?> msg, String consumerClassName, String queueName);

    /**
     * 插入一条消费记录
     *
     * @param msg       消息
     * @param consumer  消费者
     * @param queueName rabbitmq 队列名称
     * @return
     */
    MsgConsumePO insert(Msg<?> msg, String consumer, String queueName);

    /**
     * 将消息消费记录状态置为成功
     *
     * @param id
     * @param curStatus 当前状态
     */
    void updateStatusSuccess(String id, int curStatus);

    /**
     * 将消息消费记录状态置为失败
     *
     * @param id            记录id
     * @param curStatus     当前状态
     * @param failMsg       失败原因
     * @param consumerRetry 是否还需要消费重试
     * @param nextRetryTime 下次消费重试时间
     */
    void updateStatusFail(String id, int curStatus, String failMsg, boolean consumerRetry, LocalDateTime nextRetryTime);
}
