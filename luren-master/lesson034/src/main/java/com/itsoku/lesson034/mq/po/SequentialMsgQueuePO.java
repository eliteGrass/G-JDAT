package com.itsoku.lesson034.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 12:51 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@TableName("t_sequential_msg_queue_lesson034")
public class SequentialMsgQueuePO {

    /**
     * id 主键
     */
    private String id;

    /**
     * 消息组id
     */
    private String groupId;

    /**
     * 消费位置
     */
    private Long numbering;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 消息json格式
     */
    private String msgJson;
}
