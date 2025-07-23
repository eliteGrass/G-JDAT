package com.itsoku.lesson073;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 73.并发编程有多难？值得反复研究的一个案例
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/13 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson073.mapper")
public class Lesson073Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson073Application.class, args);
    }

}
