package com.itsoku.lesson042.gateway;

import cn.hutool.core.net.URLEncodeUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 18:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = getToken(request);
        if (!"itsoku".equals(token)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            DataBuffer dataBuffer = response.bufferFactory().wrap("Invalid token".getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }

        //放入公共请参数到请求头中，向下层服务中传递
        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header("itsoku-header-user_id", "1001");
        mutate.header("itsoku-header-user_name", URLEncodeUtil.encode("我是路人,通过getay传递过来的"));
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private String getToken(ServerHttpRequest request) {
        return request.getHeaders().getFirst("token");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
