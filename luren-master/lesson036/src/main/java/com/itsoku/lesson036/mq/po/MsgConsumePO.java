package com.itsoku.lesson036.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:02 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_msg_consume_lesson036")
@Data
public class MsgConsumePO {
    /**
     * 消息id
     */
    private String id;

    /**
     * 生产者名称
     */
    private String producer;

    /**
     * 生产者这边消息的唯一标识
     */
    private String producerBusId;

    /**
     * 消费者完整类名
     */
    private String consumerClassName;
    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 消息体
     */
    private String bodyJson;

    /**
     * 消息状态，0：待消费，1：消费成功，2：消费失败
     */
    private Integer status;

    /**
     * status=2时，记录消费失败的原因
     */
    private String failMsg;

    /**
     * 已消费失败次数
     */
    private Integer failCount;

    /**
     * 消费失败后，是否还需要重试？1：是，0：否
     */
    private Integer consumeRetry;

    /**
     * 投递失败后，下次重试时间
     */
    private LocalDateTime nextRetryTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建时间
     */
    private LocalDateTime updateTime;
}
