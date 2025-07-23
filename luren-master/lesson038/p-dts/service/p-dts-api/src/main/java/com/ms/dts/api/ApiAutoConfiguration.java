package com.ms.dts.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： p-dts-api自动装配<br>
 * <b>time</b>：2018-07-27 14:46 <br>
 * <b>author</b>： ready likun_557@163.com
 */
@EnableDiscoveryClient
@ConditionalOnProperty(prefix = ApiAutoConfiguration.PREFIX, name = "enabled", matchIfMissing = true)
@Configuration
@EnableFeignClients
@Slf4j
public class ApiAutoConfiguration {
    public static final String PREFIX = "ms.dts.api";
}
