package com.itsoku.lesson048;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第48节 电商系统-资金账户表设计 及 应用实战
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson048.mapper")
public class Lesson048Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson048Application.class, args);
    }

}
