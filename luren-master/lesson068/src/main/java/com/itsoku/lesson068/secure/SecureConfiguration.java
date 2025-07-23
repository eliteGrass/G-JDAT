package com.itsoku.lesson068.secure;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/3 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@EnableConfigurationProperties(SecureProperties.class)
public class SecureConfiguration {
    @Autowired
    private SecureProperties secureProperties;

    @Bean
    public AES aes() {
        return SecureUtil.aes(this.secureProperties.getKey().getBytes(StandardCharsets.UTF_8));
    }
}
