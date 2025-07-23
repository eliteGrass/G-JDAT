package com.itsoku.lesson041.lock;

import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>：分布式锁，默认采用 Redisson <br>
 * <b>time</b>：2024/5/23 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@EnableConfigurationProperties(DistributeLockProperties.class)
public class DistributeLockConfiguration {

    @Bean
    public DistributeLock distributeLock(RedissonClient redisson, DistributeLockProperties distributeLockProperties) {
        return new RedisDistributeLock(redisson, distributeLockProperties);
    }

    @Bean
    public LockAspect lockAspect(DistributeLock distributeLock) {
        return new LockAspect(distributeLock);
    }
}
