package com.itsoku.lesson036.mq.sender.retry;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 13:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class MqSendRetryResult {
    //是否需要重试
    private boolean retry;
    //下次重试时间
    private LocalDateTime nextRetryTime;
}
