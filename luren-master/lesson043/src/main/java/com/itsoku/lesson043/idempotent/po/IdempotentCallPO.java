package com.itsoku.lesson043.idempotent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 幂等调用辅助表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 16:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@TableName("t_idempotent_call")
public class IdempotentCallPO {

    //id，主键
    private String id;

    //请求id，唯一
    private String requestId;

    //状态，0：待处理，1：处理成功，-1：处理失败
    private Integer status;

    //请求参数json格式
    private String requestJson;

    //响应数据json格式
    private String responseJson;

    //版本号，用于乐观锁，每次更新+1
    @Version
    private Long version;

    //创建时间
    private LocalDateTime createTime;

    //最后更新时间
    private LocalDateTime updateTime;
}
