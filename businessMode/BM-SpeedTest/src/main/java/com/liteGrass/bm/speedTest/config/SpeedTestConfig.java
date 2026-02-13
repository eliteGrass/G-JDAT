package com.liteGrass.bm.speedTest.config;


import com.liteGrass.bm.speedTest.properties.SpeedTestProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: SpeedTestConfig
 * @Author: liteGrass
 * @Date: 2025/10/29 9:47
 * @Description: 测速配置类
 */
@Configuration
public class SpeedTestConfig {

    // 绑定配置文件参数
    @Bean
    @ConfigurationProperties(prefix = "speed-test")
    public SpeedTestProperties speedTestProperties() {
        return new SpeedTestProperties();
    }

    // 测速专用线程池（与业务线程池隔离）
    @Bean("speedTestExecutor")
    public ThreadPoolTaskExecutor speedTestExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);         // 核心线程数
        executor.setMaxPoolSize(10);        // 最大线程数
        executor.setQueueCapacity(50);      // 队列容量
        executor.setThreadNamePrefix("speed-test-"); // 线程名前缀
        // 拒绝策略：超出并发时丢弃（避免影响业务）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.initialize();
        return executor;
    }
}
