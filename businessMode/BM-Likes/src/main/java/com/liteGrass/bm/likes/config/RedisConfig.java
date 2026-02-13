package com.liteGrass.bm.likes.config;


import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @ClassName: RedisConfig
 * @Author: liteGrass
 * @Date: 2025/8/22 23:33
 * @Description: redis配置类
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);
        t.setKeySerializer(RedisSerializer.string());
        t.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        t.setHashKeySerializer(RedisSerializer.string());
        t.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return t;
    }



}
