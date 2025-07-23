package com.itsoku.lesson042.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 第42节 微服务中如何传递公共参数？
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/23 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@EnableFeignClients
public class Lesson042Service2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson042Service2Application.class, args);
    }

}
