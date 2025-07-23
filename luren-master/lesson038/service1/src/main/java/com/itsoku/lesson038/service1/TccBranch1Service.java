package com.itsoku.lesson038.service1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 第38节 分布式事务：通用的TCC分布式事务生产级代码落地实战
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/8 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = "com.itsoku.lesson038.service1.mapper")
public class TccBranch1Service {

    public static void main(String[] args) {
        SpringApplication.run(TccBranch1Service.class, args);
    }

}
