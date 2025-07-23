package com.itsoku.lesson046;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 46.MyBatis进阶：封装MyBatis，实现通用的无SQL版CRUD功能
 *
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson046.mapper")
public class Lesson046Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson046Application.class, args);
    }

}
