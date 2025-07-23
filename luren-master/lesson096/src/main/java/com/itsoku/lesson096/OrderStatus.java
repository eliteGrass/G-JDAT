package com.itsoku.lesson096;

import java.util.Objects;

/**
 * 订单状态
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum OrderStatus {
    INIT(0, "待支付"),
    PAID(100, "已付款"),
    SHIPPED(200, "已发货"),
    FINISHED(300, "已结束");

    private int status;

    private String description;

    OrderStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus get(Integer status) {
        for (OrderStatus value : values()) {
            if (Objects.equals(value.getStatus(), status)) {
                return value;
            }
        }
        return null;
    }
}
