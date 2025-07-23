package com.itsoku.lesson034.lock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/4 21:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class DistributeLockConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DistributeLock distributeLock() {
        return new DefaultDistributeLock();
    }
}
