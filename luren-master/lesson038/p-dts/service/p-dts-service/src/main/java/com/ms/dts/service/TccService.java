package com.ms.dts.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 第38节 分布式事务：通用的TCC分布式事务生产级代码落地实战
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@MapperScan(annotationClass = Mapper.class)
@EnableScheduling
public class TccService {

    public static void main(String[] args) {
        SpringApplication.run(TccService.class, args);
    }

}

