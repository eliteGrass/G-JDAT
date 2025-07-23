package com.itsoku.lesson034.dto;

import lombok.Builder;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 15:14 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@Builder
public class OrderMsg {
    private String orderId;
    private String type;
}
