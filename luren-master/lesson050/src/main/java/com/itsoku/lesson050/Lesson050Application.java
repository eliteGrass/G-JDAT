package com.itsoku.lesson050;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第50节 SpringBoot多线程事务工具类，太好用了
 *
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/14 19:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson050.mapper")
public class Lesson050Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson050Application.class, args);
    }

}
