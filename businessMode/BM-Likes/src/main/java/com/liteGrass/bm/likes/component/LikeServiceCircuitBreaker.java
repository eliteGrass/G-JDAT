package com.liteGrass.bm.likes.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: LikeServiceCircuitBreaker
 * @Author: liteGrass  
 * @Date: 2025/8/24
 * @Description: 点赞服务熔断器，提供系统保护机制
 */
@Slf4j
@Component
public class LikeServiceCircuitBreaker {
    
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private volatile boolean circuitOpen = false;
    private volatile long lastFailureTime = 0;
    
    // 熔断器配置
    private static final int FAILURE_THRESHOLD = 10; // 失败阈值
    private static final long RECOVERY_TIMEOUT = 60000; // 恢复时间：60秒
    
    /**
     * 检查是否允许请求通过
     */
    public boolean allowRequest() {
        if (!circuitOpen) {
            return true;
        }
        
        // 半开状态检查
        if (System.currentTimeMillis() - lastFailureTime > RECOVERY_TIMEOUT) {
            circuitOpen = false;
            failureCount.set(0);
            log.info("点赞服务熔断器恢复");
            return true;
        }
        return false;
    }
    
    /**
     * 记录失败
     */
    public void recordFailure() {
        lastFailureTime = System.currentTimeMillis();
        int currentFailures = failureCount.incrementAndGet();
        
        if (currentFailures >= FAILURE_THRESHOLD && !circuitOpen) {
            circuitOpen = true;
            log.warn("点赞服务熔断器开启，失败次数: {}", currentFailures);
        }
    }
    
    /**
     * 记录成功
     */
    public void recordSuccess() {
        if (circuitOpen) {
            circuitOpen = false;
            failureCount.set(0);
            log.info("点赞服务熔断器关闭");
        } else if (failureCount.get() > 0) {
            failureCount.decrementAndGet();
        }
    }
    
    /**
     * 获取当前状态
     */
    public boolean isCircuitOpen() {
        return circuitOpen;
    }
    
    /**
     * 获取失败次数
     */
    public int getFailureCount() {
        return failureCount.get();
    }
}