package com.itsoku.lesson042.common.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 15:05 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class SystemContextAutoConfiguration {
    @Bean
    public SystemContextFilter systemContextFilter() {
        return new SystemContextFilter();
    }

    @Bean
    @ConditionalOnClass(FeignClient.class)
    public SystemContextRequestInterceptor systemContextRequestInterceptor() {
        return new SystemContextRequestInterceptor();
    }

    @Bean
    public SystemContextRestTemplateRequestCustomizer systemContextRestTemplateRequestCustomizer() {
        return new SystemContextRestTemplateRequestCustomizer();
    }
}
