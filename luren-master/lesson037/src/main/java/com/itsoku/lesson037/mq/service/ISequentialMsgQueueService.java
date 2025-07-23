package com.itsoku.lesson037.mq.service;

import com.itsoku.lesson037.common.service.IBaseService;
import com.itsoku.lesson037.mq.po.SequentialMsgQueuePO;

/**
 * 顺序消息排队表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */

public interface ISequentialMsgQueueService extends IBaseService<SequentialMsgQueuePO> {
    /**
     * 压入一条顺序消息
     *
     * @param queueName 消费者队列名称
     * @param groupId   组id
     * @param numbering 消息编号
     * @param msgJson   顺序消息json格式
     * @return 记录id
     */
    SequentialMsgQueuePO push(String groupId, Long numbering, String queueName, String msgJson);

    /**
     * 获取队列头部记录
     *
     * @param groupId   组id
     * @param queueName 消费者队列
     * @return
     */
    SequentialMsgQueuePO getFirst(String groupId, String queueName);

    /**
     * 删除记录
     *
     * @param id
     */
    void delete(String id);

}
