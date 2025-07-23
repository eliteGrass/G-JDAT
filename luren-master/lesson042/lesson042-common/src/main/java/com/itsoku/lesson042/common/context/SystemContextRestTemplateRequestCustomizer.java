package com.itsoku.lesson042.common.context;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.client.ClientHttpRequest;

/**
 * 会在 RestTemplate 发送请求前，将公共参数放到请求头中，进行传递
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 15:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SystemContextRestTemplateRequestCustomizer implements RestTemplateRequestCustomizer {
    @Override
    public void customize(ClientHttpRequest request) {
        // 从ThreadLocal中拿到公共上线文参数，放到 RestTemplate 请求头中
        for (Pair<String, String> header : SystemContextHolder.getSystemContext().toHeaders()) {
            request.getHeaders().add(header.getKey(), header.getValue());
        }
    }
}
