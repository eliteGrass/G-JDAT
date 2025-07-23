package com.itsoku.lesson042.common.context;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.tuple.Pair;

/**
 * OpenFeign请求拦截器，会在OpenFeign发送请求前，将公共参数放到请求头中，进行传递
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 14:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SystemContextRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从ThreadLocal中拿到公共上线文参数，放到 OpenFeign 请求头中
        for (Pair<String, String> header : SystemContextHolder.getSystemContext().toHeaders()) {
            template.header(header.getKey(), header.getValue());
        }
    }
}
