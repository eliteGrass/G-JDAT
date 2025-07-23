package com.itsoku.lesson090;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 90.Spring事务失效，常见的几种场景，带你精通Spring事务
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 22:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson090.mapper")
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class Lesson090Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson090Application.class, args);
    }

}
