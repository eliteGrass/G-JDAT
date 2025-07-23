package com.itsoku.lesson037.mq.sender;

import com.itsoku.lesson037.mq.dto.Msg;
import com.itsoku.lesson037.mq.po.MsgPO;
import com.itsoku.lesson037.utils.CollUtils;

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
     * @param msgBody 消息体
     * @param <T>
     * @return
     */
    <T> Msg<T> build(T msgBody);

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
     * 发送顺序消息，可确保（busId+exchange+routingKey）相同的消息的顺序性
     *
     * @param busId
     * @param exchange
     * @param routingKey
     * @param msg
     */
    void sendSequentialMsg(String busId, String exchange, String routingKey, Msg<?> msg);

    /**
     * 发送顺序消息，可确保（busId+exchange+routingKey）相同的情况下，消息的顺序性
     * 这里我们使用的是rabbitmq，rabbitmq中 exchange+routingKey 可以确保消息被路由到相同的队列
     * <p>
     * 这里为什么要加入busId？
     * 以订单消息举例，所有订单消息都会进入同样的队列，订单有3种类型消息（创建、支付、发货）
     * 同一个订单的这3种类型的消息是要有序的，但是不同订单之间，他们不需要有序，所以这里加了个busId，此时就可以将订单id作为busId
     *
     * @param busId      业务id
     * @param exchange
     * @param routingKey
     * @param msgBody    消息体
     */
    default void sendSequentialWithBody(String busId, String exchange, String routingKey, Object msgBody) {
        this.sendSequentialMsg(busId, exchange, routingKey, this.build(msgBody));
    }

    /**
     * 投递重试
     *
     * @param msgPO
     */
    void sendRetry(MsgPO msgPO);

}
