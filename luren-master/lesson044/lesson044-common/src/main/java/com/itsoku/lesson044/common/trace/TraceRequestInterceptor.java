package com.itsoku.lesson044.common.trace;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 拦截 OpenFeign 所有请求，在 OpenFeign 发送请求前，将traceId放到请求头中，传递给被调用者
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 14:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TraceRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从ThreadLocal中取出traceId，放到 OpenFeign 请求头中，传给被调用者
        template.header(TraceUtils.TRACE_ID, TraceUtils.getTraceId());
    }
}
