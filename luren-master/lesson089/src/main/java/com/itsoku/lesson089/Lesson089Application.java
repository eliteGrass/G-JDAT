package com.itsoku.lesson089;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis模糊查询，千万不要再用${}了，容易搞出大事故
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson089.mapper")
public class Lesson089Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson089Application.class, args);
    }

}
