package com.itsoku.lesson068.secure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/3 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@ConfigurationProperties("secure")
public class SecureProperties {
    /**
     * 秘钥(长度只能是 128、192或256位，一个普通字符是8位)
     */
    private String key;
}
