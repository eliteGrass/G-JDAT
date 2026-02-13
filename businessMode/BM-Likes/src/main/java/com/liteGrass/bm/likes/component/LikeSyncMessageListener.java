package com.liteGrass.bm.likes.component;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liteGrass.bm.likes.entity.BmLikeCount;
import com.liteGrass.bm.likes.mapper.BmLikeMapper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: LikeSyncMessageListener
 * @Author: liteGrass
 * @Date: 2025/8/24
 * @Description: 点赞同步消息监听器，处理缓存过期事件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LikeSyncMessageListener implements MessageListener {
    
    private final BmLikeMapper bmLikeMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final LikeServiceCircuitBreaker circuitBreaker;
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (!circuitBreaker.allowRequest()) {
            log.warn("熔断器开启，跳过消息处理");
            return;
        }
        
        try {
            String expiredKey = new String(message.getBody());
            
            // 只处理点赞相关的key
            if (!expiredKey.startsWith("likeCount:")) {
                return;
            }
            
            // 异步处理同步逻辑
            processExpiredKey(expiredKey);
            
            circuitBreaker.recordSuccess();
        } catch (Exception e) {
            log.error("处理过期消息失败", e);
            circuitBreaker.recordFailure();
        }
    }
    
    /**
     * 处理过期的缓存key
     */
    private void processExpiredKey(String expiredKey) {
        // 获取对应的分片队列key
        String[] parts = expiredKey.split(":");
        if (parts.length < 2) {
            return;
        }
        
        try {
            Long bizId = Long.valueOf(parts[1]);
            String dirtyQueueKey = getDirtyQueueKey(bizId);
            
            // 首先判断过期key是否在对应分片的脏数据队列中
            Double likeCount = redisTemplate.opsForZSet().score(dirtyQueueKey, expiredKey);
            if (ObjectUtil.isNull(likeCount)) {
                return;
            }
            
            // 使用分布式锁确保只有一个实例处理
            RLock lock = redissonClient.getLock("expire-sync:" + expiredKey);
            try {
                if (lock.tryLock(1, 10, TimeUnit.SECONDS)) {
                    // 再次检查是否存在（双重检查）
                    likeCount = redisTemplate.opsForZSet().score(dirtyQueueKey, expiredKey);
                    if (ObjectUtil.isNull(likeCount)) {
                        return;
                    }
                    
                    // 同步到数据库
                    bmLikeMapper.update(Wrappers.lambdaUpdate(BmLikeCount.class)
                            .eq(BmLikeCount::getBizId, bizId)
                            .set(BmLikeCount::getLikeNum, likeCount.longValue()));
                    
                    // 先从脏数据队列中删除
                    redisTemplate.opsForZSet().remove(dirtyQueueKey, expiredKey);
                    
                    // 延迟双删优化：删除可能的老缓存
                    delayedCacheDelete(expiredKey);
                    
                    log.debug("消息监听器同步成功: {}, 点赞数: {}", expiredKey, likeCount);
                }
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            log.error("处理过期key失败: {}", expiredKey, e);
        }
    }
    
    /**
     * 延迟删除缓存
     */
    private void delayedCacheDelete(String cacheKey) {
        // 这里可以使用线程池异步执行，或者使用延时队列
        new Thread(() -> {
            try {
                Thread.sleep(100); // 等待100ms
                redisTemplate.delete(cacheKey);
                log.debug("延迟双删执行: {}", cacheKey);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("延迟删除被中断: {}", cacheKey);
            }
        }).start();
    }
    
    /**
     * 获取分片队列key
     */
    private String getDirtyQueueKey(Long bizId) {
        int shardIndex = Math.abs(bizId.hashCode()) % 16; // 与主服务保持一致
        return "dirty_likes_shard_" + shardIndex;
    }
}