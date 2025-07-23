package com.itsoku.lesson037;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第37节 分布式事务-MQ最终一致性-实现提现到微信钱包
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/8 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itsoku.lesson037.mapper")
public class Lesson037Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson037Application.class, args);
    }

}
