package com.itsoku.lesson036.mq.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 账户表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_idempotent_lesson036")
@Data
public class IdempotentPO {
    //id，主键
    private String id;

    //幂等key，唯一
    private String idempotentKey;
}
