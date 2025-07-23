package com.itsoku.lesson061;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第61节 使用MySQL，实现一个高性能，分布式id生成器
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/67/6 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson061.mapper")
public class Lesson061Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson061Application.class, args);
    }

}
