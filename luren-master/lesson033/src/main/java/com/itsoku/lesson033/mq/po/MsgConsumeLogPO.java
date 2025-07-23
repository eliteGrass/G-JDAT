package com.itsoku.lesson033.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:02 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_msg_consume_log_lesson033")
@Data
public class MsgConsumeLogPO {
    /**
     * 消息id
     */
    private String id;

    /**
     * 消息和消费者关联记录
     */
    private String msgConsumeId;

    /**
     * 消费状态，1：消费成功，2：消费失败
     */
    private Integer status;

    /**
     * status=2 时，记录消息消费失败的原因
     */
    private String failMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
