package com.itsoku.lesson037.mq.service;

import com.itsoku.lesson037.common.service.IBaseService;
import com.itsoku.lesson037.mq.po.MsgPO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:06 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IMsgService extends IBaseService<MsgPO> {
    /**
     * 批量插入消息
     *
     * @param exchange        交换机
     * @param routingKey      路由key
     * @param expectSendTime  期望发送时间
     * @param msgBodyJsonList
     * @return
     */
    List<MsgPO> batchInsert(String exchange, String routingKey, LocalDateTime expectSendTime, List<String> msgBodyJsonList);

    /**
     * 将消息状态置为成功
     *
     * @param msgPO
     */
    void updateStatusSuccess(MsgPO msgPO);

    /**
     * 将消息状态置为失败
     *
     * @param msgPO
     * @param failMsg       失败原因
     * @param sendRetry     是否还需要重试
     * @param nextRetryTime 下次重试发送时间
     */
    void updateStatusFail(MsgPO msgPO, String failMsg, boolean sendRetry, LocalDateTime nextRetryTime);
}
