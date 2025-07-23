package com.itsoku.lesson036.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 12:51 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@TableName("t_sequential_msg_consume_position_lesson036")
public class SequentialMsgConsumePositionPO {

    /**
     * id 主键
     */
    private String id;

    /**
     * 消息组id
     */
    private String groupId;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 消费位置
     */
    private Long consumeNumbering;

    /**
     * 版本号，每次更新+1
     */
    @Version
    private Long version;
}
