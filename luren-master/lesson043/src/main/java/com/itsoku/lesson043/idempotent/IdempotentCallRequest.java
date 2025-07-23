package com.itsoku.lesson043.idempotent;

import lombok.*;

/**
 * 幂等调用请求体
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@Builder
public class IdempotentCallRequest<T> {
    /**
     * 请求id，唯一
     */
    private String requestId;
    /**
     * 请求参数
     */
    private T data;
}
