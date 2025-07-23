package com.itsoku.lesson044.common.trace;

import org.slf4j.MDC;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/24 11:26 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TraceUtils {
    public static final String TRACE_ID = "traceId";
    public static ThreadLocal<String> traceIdThreadLocal = new ThreadLocal<>();

    public static String getTraceId() {
        return traceIdThreadLocal.get();
    }

    public static void setTraceId(String traceId) {
        traceIdThreadLocal.set(traceId);
        MDC.put(TRACE_ID, traceId);
    }

    public static void removeTraceId() {
        traceIdThreadLocal.remove();
        MDC.remove(TRACE_ID);
    }

}
