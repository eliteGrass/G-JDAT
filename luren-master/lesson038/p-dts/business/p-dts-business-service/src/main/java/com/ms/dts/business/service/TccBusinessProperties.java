package com.ms.dts.business.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2019-01-11 09:10 <br>
 * <b>author</b>： ready likun_557@163.com
 */
@ConfigurationProperties(prefix = TccBusinessProperties.PREFIX)
@Getter
@Setter
public class TccBusinessProperties {
    public static final String PREFIX = "ms.dts.business";
}
