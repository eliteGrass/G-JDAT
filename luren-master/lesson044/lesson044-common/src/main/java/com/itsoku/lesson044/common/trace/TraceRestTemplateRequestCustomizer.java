package com.itsoku.lesson044.common.trace;

import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.client.ClientHttpRequest;

/**
 * 拦截RestTemplate所有请求，在 RestTemplate 发送请求前，将traceId放到请求头中，传递给被调用者
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 15:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TraceRestTemplateRequestCustomizer implements RestTemplateRequestCustomizer {
    @Override
    public void customize(ClientHttpRequest request) {
        // 从ThreadLocal中取出traceId，放到 RestTemplate 请求头中，传给被调用者
        request.getHeaders().add(TraceUtils.TRACE_ID, TraceUtils.getTraceId());
    }
}
