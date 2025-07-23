package com.itsoku.lesson030.mq.sender;

import com.itsoku.lesson030.mq.po.MsgPO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 消息发送器
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 12:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IMsgSender {
    /**
     * 批量发送消息
     *
     * @param msgList
     */
    void send(List<Object> msgList);

    /**
     * 发送单条消息
     *
     * @param msg
     */
    default void send(Object msg) {
        Objects.nonNull(msg);
        this.send(Arrays.asList(msg));
    }

    /**
     * 投递重试
     *
     * @param msgPO
     */
    void sendRetry(MsgPO msgPO);
}
