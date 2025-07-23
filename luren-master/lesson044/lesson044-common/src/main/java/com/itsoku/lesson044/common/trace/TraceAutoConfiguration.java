package com.itsoku.lesson044.common.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/24 12:06 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration(proxyBeanMethods = false)
public class TraceAutoConfiguration {
    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    @Bean
    public ResultTraceIdAspect fillRequestIdAspect() {
        return new ResultTraceIdAspect();
    }

    @Bean
    public TraceRestTemplateRequestCustomizer traceRestTemplateRequestCustomizer(){
        return new TraceRestTemplateRequestCustomizer();
    }

    @Bean
    public TraceRequestInterceptor traceRequestInterceptor(){
        return new TraceRequestInterceptor();
    }
}
