package com.itsoku.lesson033.mq.consumer.retry;

import com.itsoku.lesson033.mq.po.MsgConsumePO;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 16:27 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface MsgConsumeRetry {
    /**
     * 获取消息消费重试的一些信息（如：是否需要重试，下次重试时间）
     *
     * @param msgConsumePO
     * @return
     */
    MsgConsumeRetryResult getRetryResult(MsgConsumePO msgConsumePO);
}
