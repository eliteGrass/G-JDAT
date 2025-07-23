package com.itsoku.lesson037.mq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/5 12:23 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@ConfigurationProperties(prefix = "mq")
public class MqProperties {
    /**
     * 设置用于处理延迟消息的延迟队列大小
     */
    private int delayMsgDelayQueueCapacity = 1000000;
    /**
     * 设置用于处理投递重试的延迟队列大小
     */
    private int delaySendRetryDelayQueueCapacity = 1000000;

    /**
     * 生产者，可以用一个有意义的名称，比如取服务名称，这样消费者知道这个消息来自于哪里
     */
    private String producer;
}
