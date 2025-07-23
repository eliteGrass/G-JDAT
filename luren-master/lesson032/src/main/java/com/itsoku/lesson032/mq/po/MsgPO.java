package com.itsoku.lesson032.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:02 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_msg_lesson032")
@Data
public class MsgPO {
    /**
     * 消息id
     */
    private String id;

    /**
     * 消息体
     */
    private String bodyJson;

    /**
     * 消息期望投递时间，大于当前时间，则为延迟消息，否则会立即投递
     */
    private LocalDateTime expectSendTime;

    /**
     * 消息实际投递时间
     */
    private LocalDateTime actualSendTime;

    /**
     * 消息状态，0：待投递到mq，1：投递成功，2：投递失败
     */
    private Integer status;

    /**
     * status=0时，记录消息投递失败的原因
     */
    private String failMsg;

    /**
     * 已投递失败次数
     */
    private Integer failCount;

    /**
     * 投递MQ失败了，是否还需要重试？1：是，0：否
     */
    private Integer sendRetry;

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
