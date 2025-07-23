package com.itsoku.lesson037.mq.enums;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:04 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum MsgConsumerLogStatusEnum {
    SUCCESS(1, "消费成功"),
    FAIL(2, "消费失败");

    //状态
    private Integer status;

    //描述
    private String description;

    MsgConsumerLogStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
