package com.itsoku.lesson033.mq.dto;

import lombok.Data;

import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 14:30 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class Msg<T> {
    /**
     * 生产者名称
     */
    private String producer;
    /**
     * 生产者这边消息的唯一标识
     */
    private String producerBusId;
    /**
     * 消息体，主要是消息的业务数据
     */
    private T body;

    public Msg() {
    }

    public Msg(String producer, String producerBusId, T body) {
        this.producer = producer;
        this.producerBusId = producerBusId;
        this.body = body;
    }

    public static <T> Msg<T> msg(String producer, String producerBusId, T body) {
        Objects.nonNull(producer);
        Objects.nonNull(producerBusId);
        Objects.nonNull(body);
        return new Msg<>(producer, producerBusId, body);
    }
}
