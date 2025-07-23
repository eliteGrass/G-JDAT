package com.itsoku.lesson033;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第33节 MQ消息幂等消费 & 消费失败自动重试通用方案 & 代码落地
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/8 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson033.mapper")
public class Lesson033Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson033Application.class, args);
    }

}
