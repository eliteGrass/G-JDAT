package com.itsoku.lesson042.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 第42节 微服务中如何传递公共参数？
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/23 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
public class Lesson042GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lesson042GatewayApplication.class, args);
    }
}
