package com.itsoku.lesson033.mq.sender;

import com.itsoku.lesson033.mq.dto.Msg;
import com.itsoku.lesson033.mq.po.MsgPO;
import com.itsoku.lesson033.utils.CollUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送器
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 12:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IMsgSender {

    /**
     * 构建一条消息
     *
     * @param body 消息体
     * @param <T>
     * @return
     */
    <T> Msg<T> build(T body);

    /**
     * 批量发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param msgList
     */
    void send(String exchange, String routingKey, List<Msg<?>> msgList);

    /**
     * 发送单条消息
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param msg
     */
    default void send(String exchange, String routingKey, Msg<?> msg) {
        Objects.nonNull(msg);
        this.send(exchange, routingKey, Arrays.asList(msg));
    }

    /**
     * 批量投递延迟消息
     *
     * @param exchange      交换机
     * @param routingKey    路由key
     * @param delayTime     延迟时间
     * @param delayTimeUnit 延迟时间单位
     * @param msgList       消息
     */
    void send(String exchange, String routingKey, long delayTime, TimeUnit delayTimeUnit, List<Msg<?>> msgList);

    /**
     * 发送单条延迟消息
     *
     * @param exchange      交换机
     * @param routingKey    路由key
     * @param delayTime     延迟时间
     * @param delayTimeUnit 延迟时间单位
     * @param msg           消息
     */
    default void send(String exchange, String routingKey, long delayTime, TimeUnit delayTimeUnit, Msg<?> msg) {
        this.send(exchange, routingKey, delayTime, delayTimeUnit, Arrays.asList(msg));
    }


    /**
     * 批量发送消息
     *
     * @param exchange    交换机
     * @param routingKey  路由key
     * @param msgBodyList 消息体列表
     */
    default void sendWithBody(String exchange, String routingKey, List<?> msgBodyList) {
        this.send(exchange, routingKey, CollUtils.convertList(msgBodyList, this::build));
    }

    /**
     * 发送单条消息
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param msgBody
     */
    default void sendWithBody(String exchange, String routingKey, Object msgBody) {
        Objects.nonNull(msgBody);
        this.sendWithBody(exchange, routingKey, Arrays.asList(msgBody));
    }

    /**
     * 批量投递延迟消息
     *
     * @param exchange      交换机
     * @param routingKey    路由key
     * @param delayTime     延迟时间
     * @param delayTimeUnit 延迟时间单位
     * @param msgBodyList   消息体列表
     */
    default void sendWithBody(String exchange, String routingKey, long delayTime, TimeUnit delayTimeUnit, List<?> msgBodyList) {
        this.send(exchange, routingKey, delayTime, delayTimeUnit, CollUtils.convertList(msgBodyList, this::build));
    }

    /**
     * 发送单条延迟消息
     *
     * @param exchange      交换机
     * @param routingKey    路由key
     * @param delayTime     延迟时间
     * @param delayTimeUnit 延迟时间单位
     * @param msgBody       消息
     */
    default void sendWithBody(String exchange, String routingKey, long delayTime, TimeUnit delayTimeUnit, Object msgBody) {
        this.sendWithBody(exchange, routingKey, delayTime, delayTimeUnit, Arrays.asList(msgBody));
    }

    /**
     * 投递重试
     *
     * @param msgPO
     */
    void sendRetry(MsgPO msgPO);
}
