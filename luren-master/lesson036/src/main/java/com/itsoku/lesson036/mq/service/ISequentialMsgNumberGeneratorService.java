package com.itsoku.lesson036.mq.service;

import com.itsoku.lesson036.common.service.IBaseService;
import com.itsoku.lesson036.mq.po.SequentialMsgNumberGeneratorPO;

/**
 * 顺序消息编号生成器
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */

public interface ISequentialMsgNumberGeneratorService extends IBaseService<SequentialMsgNumberGeneratorPO> {
    /**
     * 获取一个编号，相同的groupId中的编号是从1连续递增的
     *
     * @param groupId
     * @return
     */
    long get(String groupId);
}
