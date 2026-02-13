package com.liteGrass.bm.speedTest.service;


import com.liteGrass.bm.speedTest.properties.SpeedTestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SpeedTestService
 * @Author: liteGrass
 * @Date: 2025/10/29 9:50
 * @Description:
 */
@Service
public class SpeedTestService {

    @Autowired
    private SpeedTestProperties properties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Redis键前缀（避免与其他业务冲突）
    private static final String REDIS_KEY_PREFIX = "speed_test:ip:";

    /**
     * 生成下载测试数据（内存中随机生成）
     */
    public InputStream generateDownloadData(int sizeMb) {
        int dataSizeBytes = sizeMb * 1024 * 1024;
        byte[] data = new byte[dataSizeBytes];
        new SecureRandom().nextBytes(data); // 生成随机字节
        return new ByteArrayInputStream(data);
    }

    /**
     * 验证上传文件大小
     */
    public boolean validateUploadFile(MultipartFile file) {
        return file.getSize() <= properties.getUploadMaxSizeMb() * 1024 * 1024;
    }

    /**
     * 检查单IP请求频率限制（基于Redis，支持分布式）
     * @param ip 客户端IP
     * @return true：允许请求；false：超过限制
     */
    @Async("speedTestExecutor") // 异步执行，不阻塞主线程
    public boolean checkRateLimit(String ip) {
        String redisKey = REDIS_KEY_PREFIX + ip;

        // 原子递增计数（INCR命令）
        Long count = redisTemplate.opsForValue().increment(redisKey, 1);

        // 首次计数时设置过期时间（与限制周期一致）
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, properties.getRateLimitPeriodSeconds(), TimeUnit.SECONDS);
        }

        // 判断是否超过限制
        return count != null && count <= properties.getMaxRequestsPerIp();
    }
}
