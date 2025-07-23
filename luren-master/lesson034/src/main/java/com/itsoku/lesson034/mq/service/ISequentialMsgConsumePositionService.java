package com.itsoku.lesson034.mq.service;

import com.itsoku.lesson034.common.service.IBaseService;
import com.itsoku.lesson034.mq.po.SequentialMsgConsumePositionPO;

/**
 * 顺序消息消费信息表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */

public interface ISequentialMsgConsumePositionService extends IBaseService<SequentialMsgConsumePositionPO> {

    /**
     * 根据 groupId  & queueName 获取记录，不存在的时候则创建
     *
     * @param groupId
     * @param queueName
     * @return
     */
    SequentialMsgConsumePositionPO getAndCreate(String groupId, String queueName);

}
