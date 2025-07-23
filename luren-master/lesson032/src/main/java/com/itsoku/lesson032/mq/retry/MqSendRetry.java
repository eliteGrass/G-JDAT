package com.itsoku.lesson032.mq.retry;

import com.itsoku.lesson032.mq.po.MsgPO;

/**
 * mq消息投递重试
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 13:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface MqSendRetry {
    /**
     * 获取消息重试的一些信息（如：是否需要重试，下次重试时间）
     *
     * @param msgPO
     * @return
     */
    MqSendRetryResult getRetryResult(MsgPO msgPO);
}
