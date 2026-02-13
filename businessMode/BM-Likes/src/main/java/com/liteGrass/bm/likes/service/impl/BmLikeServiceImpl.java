package com.liteGrass.bm.likes.service.impl;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liteGrass.bm.likes.component.LikeServiceCircuitBreaker;
import com.liteGrass.bm.likes.entity.BmLikeCount;
import com.liteGrass.bm.likes.mapper.BmLikeMapper;
import com.liteGrass.bm.likes.service.BmLikeService;
import jakarta.annotation.PostConstruct;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.PatternMessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @ClassName: BmLikeServiceImpl
 * @Author: liteGrass
 * @Date: 2025/8/22 23:03
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BmLikeServiceImpl extends ServiceImpl<BmLikeMapper, BmLikeCount> implements BmLikeService {

    private final BmLikeMapper bmLikeMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final LikeServiceCircuitBreaker circuitBreaker;
    
    // 实例唯一标识
    private final String instanceId = generateInstanceId();
    
    // 主实例标识
    private volatile boolean isMaster = false;
    
    // 分片数量
    private static final int SHARD_COUNT = 16;
    
    // 异步执行器 - 改为可管理的线程池
    private final ExecutorService asyncExecutor = createManagedThreadPool();
    
    // 主实例选举调度器
    private ScheduledExecutorService masterElectionScheduler;
    
    // 缓存null值的标识
    private static final Long NULL_CACHE_VALUE = -1L;
    // 缓存过期时间
    private static final Duration CACHE_EXPIRE = Duration.ofMinutes(30);
    private static final Duration NULL_CACHE_EXPIRE = Duration.ofMinutes(5);
    
    /**
     * 生成实例唯一标识
     */
    private String generateInstanceId() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            return hostName + "-" + uuid;
        } catch (Exception e) {
            return "instance-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
    
    /**
     * 创建可管理的线程池
     */
    private ExecutorService createManagedThreadPool() {
        return new ThreadPoolExecutor(
                4, 20, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                r -> {
                    Thread t = new Thread(r, "like-sync-" + instanceId + "-" + System.currentTimeMillis());
                    t.setDaemon(true);  // 设置为守护线程
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略：调用者执行
        );
    }
    
    /**
     * 应用关闭时清理资源
     */
    @jakarta.annotation.PreDestroy
    public void shutdown() {
        log.info("正在关闭点赞服务实例: {}", instanceId);
        
        // 关闭主实例选举调度器
        if (masterElectionScheduler != null && !masterElectionScheduler.isShutdown()) {
            masterElectionScheduler.shutdown();
            try {
                if (!masterElectionScheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    masterElectionScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                masterElectionScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        // 关闭异步执行器
        if (asyncExecutor != null && !asyncExecutor.isShutdown()) {
            asyncExecutor.shutdown();
            try {
                if (!asyncExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    asyncExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                asyncExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        // 释放主实例锁
        if (isMaster) {
            try {
                RLock masterLock = redissonClient.getLock("like_service_master");
                if (masterLock.isHeldByCurrentThread()) {
                    masterLock.unlock();
                    log.info("释放主实例锁: {}", instanceId);
                }
            } catch (Exception e) {
                log.warn("释放主实例锁异常", e);
            }
        }
    }

    /**
     * 初始化主实例选举
     */
    @PostConstruct
    public void initMasterElection() {
        String masterKey = "like_service_master";
        RLock masterLock = redissonClient.getLock(masterKey);
        
        // 创建主实例选举调度器
        masterElectionScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "master-election-" + instanceId);
            t.setDaemon(true);
            return t;
        });
        
        // 定时尝试成为主实例
        masterElectionScheduler.scheduleWithFixedDelay(() -> {
            try {
                if (masterLock.tryLock(0, 30, TimeUnit.SECONDS)) {
                    if (!isMaster) {
                        isMaster = true;
                        log.info("实例 {} 成为主实例", instanceId);
                        // 只有主实例监听过期事件
                        expireListen();
                    }
                    // 主实例持续续期
                    try {
                        masterLock.lock(25, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.warn("主实例续期失败", e);
                    }
                } else {
                    if (isMaster) {
                        isMaster = false;
                        log.info("实例 {} 失去主实例身份", instanceId);
                    }
                }
            } catch (Exception e) {
                log.error("主实例选举异常", e);
                isMaster = false;
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
    
    /**
     * 过期监听器 - 实时同步机制（仅主实例执行）
     */
    private void expireListen() {
        RPatternTopic topic = redissonClient.getPatternTopic("__keyevent@10__:expired");
        topic.addListener(String.class, new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence channel, String expiredKey) {
                // 只处理点赞相关的key
                if (!expiredKey.startsWith("likeCount:")) {
                    return;
                }
                
                // 首先判断过期key是否在脏数据队列中
                if (ObjectUtil.isNull(redisTemplate.opsForZSet().score("dirty_likes", expiredKey))) {
                    return;
                }
                
                // 发送到消息队列异步处理
                redisTemplate.convertAndSend("like_sync_channel", expiredKey);
            }
        });
    }

    /**
     * 获取分片队列key
     */
    private String getDirtyQueueKey(Long bizId) {
        int shardIndex = Math.abs(bizId.hashCode()) % SHARD_COUNT;
        return "dirty_likes_shard_" + shardIndex;
    }
    
    /**
     * 异步批量同步脏数据到数据库 - 兜底机制（仅主实例执行）
     */
    @Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
    @Async
    public void batchSyncDirtyData() {
        // 只有主实例执行批量同步
        if (!isMaster) {
            return;
        }
        
        if (!circuitBreaker.allowRequest()) {
            log.warn("熔断器开启，跳过批量同步任务");
            return;
        }
        
        try {
            // 并发处理多个分片
            List<CompletableFuture<Integer>> futures = new ArrayList<>();
            
            for (int i = 0; i < SHARD_COUNT; i++) {
                final int shardIndex = i;
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    return syncShardData("dirty_likes_shard_" + shardIndex);
                }, asyncExecutor);
                futures.add(future);
            }
            
            // 等待所有分片处理完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );
            
            allFutures.orTimeout(60, TimeUnit.SECONDS).join();
            
            int totalSyncCount = futures.stream()
                .mapToInt(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        log.error("获取分片同步结果失败", e);
                        return 0;
                    }
                })
                .sum();
                
            if (totalSyncCount > 0) {
                log.info("兜底批量同步任务完成，同步了 {} 条数据", totalSyncCount);
            }
            
            circuitBreaker.recordSuccess();
            
        } catch (Exception e) {
            log.error("兜底批量同步任务异常", e);
            circuitBreaker.recordFailure();
        }
    }
    
    /**
     * 同步单个分片的数据
     */
    private int syncShardData(String shardKey) {
        try {
            // 获取分片中的脏数据，每次批量处理50条
            Set<Object> dirtyKeys = redisTemplate.opsForZSet().range(shardKey, 0, 49);
            
            if (dirtyKeys == null || dirtyKeys.isEmpty()) {
                return 0;
            }
            
            int syncCount = 0;
            for (Object keyObj : dirtyKeys) {
                String expiredKey = (String) keyObj;
                RLock lock = redissonClient.getLock("batch-sync:" + expiredKey);
                
                try {
                    if (lock.tryLock(500, 5, TimeUnit.SECONDS)) {
                        // 获取对应的值
                        Double likeCount = redisTemplate.opsForZSet().score(shardKey, expiredKey);
                        if (likeCount != null) {
                            // 更新数据库
                            String[] split = StringUtil.split(expiredKey, ":");
                            if (split.length >= 2) {
                                bmLikeMapper.update(Wrappers.lambdaUpdate(BmLikeCount.class)
                                        .eq(BmLikeCount::getBizId, Long.valueOf(split[1]))
                                        .set(BmLikeCount::getLikeNum, likeCount.longValue()));
                                
                                // 从脏数据队列中删除
                                redisTemplate.opsForZSet().remove(shardKey, expiredKey);
                                syncCount++;
                                log.debug("分片批量同步成功: {}, 点赞数: {}", expiredKey, likeCount);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("分片批量同步失败: {}", expiredKey, e);
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            return syncCount;
        } catch (Exception e) {
            log.error("同步分片数据异常: {}", shardKey, e);
            return 0;
        }
    }


    /**
     * 点赞数进行添加
     *
     * @param bizId
     * @param count
     */
    public Long addLikeCount(Long bizId, Long count) {
        // 熔断器检查
        if (!circuitBreaker.allowRequest()) {
            log.warn("熔断器开启，点赞操作被拒绝, bizId: {}", bizId);
            throw new RuntimeException("服务熔断中，请稍后重试");
        }
        
        try {
            return doAddLikeCount(bizId, count);
        } catch (Exception e) {
            circuitBreaker.recordFailure();
            throw e;
        }
    }
    
    private Long doAddLikeCount(Long bizId, Long count) {
        String cacheKey = "likeCount:" + bizId;
        String dirtyQueueKey = getDirtyQueueKey(bizId);
        
        // 检查缓存是否存在，如果不存在需要先初始化
        if (!redisTemplate.hasKey(cacheKey)) {
            // 缓存不存在，需要先初始化为数据库中的值
            RLock lock = redissonClient.getLock("init-cache:" + bizId);
            try {
                if (lock.tryLock(2, 10, TimeUnit.SECONDS)) {
                    // 再次检查（双重检查）
                    if (!redisTemplate.hasKey(cacheKey)) {
                        // 从对应分片的脏数据队列获取最新值
                        Double dirtyValue = redisTemplate.opsForZSet().score(dirtyQueueKey, cacheKey);
                        Long initialValue;
                        
                        if (dirtyValue != null) {
                            // 如果脏数据队列中有数据，使用脏数据的值
                            initialValue = dirtyValue.longValue();
                            log.debug("使用脏数据队列中的值初始化缓存: {}, 值: {}", bizId, initialValue);
                        } else {
                            // 从数据库获取初始值
                            BmLikeCount likeCount = this.lambdaQuery().eq(BmLikeCount::getBizId, bizId).one();
                            initialValue = likeCount == null ? 0L : likeCount.getLikeNum();
                            log.debug("从数据库初始化缓存: {}, 值: {}", bizId, initialValue);
                        }
                        
                        // 设置初始值
                        redisTemplate.opsForValue().set(cacheKey, initialValue, CACHE_EXPIRE);
                    }
                } else {
                    log.warn("获取初始化缓存锁失败, bizId: {}", bizId);
                }
            } catch (Exception e) {
                log.error("初始化缓存异常, bizId: {}", bizId, e);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        
        // 使用优化后的Lua脚本进行原子操作
        String lua = "-- KEYS[1] : 计数器 key\n" +
                "-- KEYS[2] : 脏数据队列（ZSET）\n" +
                "-- ARGV[1] : 自增步长（可正可负，缺省为 1）\n" +
                "-- ARGV[2] : 默认初始值（如果缓存不存在）\n" +
                "\n" +
                "local counterKey = KEYS[1]\n" +
                "local dirtyZSet  = KEYS[2]\n" +
                "local delta      = tonumber(ARGV[1]) or 1\n" +
                "local defaultVal = tonumber(ARGV[2]) or 0\n" +
                "\n" +
                "-- 检查key是否存在\n" +
                "if redis.call('EXISTS', counterKey) == 0 then\n" +
                "    -- 如果不存在，先设置默认值\n" +
                "    redis.call('SET', counterKey, defaultVal)\n" +
                "end\n" +
                "\n" +
                "local newVal = redis.call('INCRBY', counterKey, delta)\n" +
                "redis.call('ZADD', dirtyZSet, newVal, counterKey)\n" +
                "\n" +
                "return newVal";
                
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(lua);
        script.setResultType(Long.class);
        
        // 获取当前缓存值作为默认值（防止并发间隙期缓存被删除）
        Long currentValue = (Long) redisTemplate.opsForValue().get(cacheKey);
        Long defaultValue = currentValue != null ? currentValue : 0L;
        
        Long countNum = redisTemplate.execute(script, 
            ListUtil.list(false, cacheKey, dirtyQueueKey), 
            count.toString(), 
            defaultValue.toString());
            
        circuitBreaker.recordSuccess();
            
        log.info("点赞数操作成功，现在的点赞数为：{}", countNum);
        return countNum;
    }

    /**
     * 根据业务id获取相应的点赞数
     *
     * @param bizId
     * @return
     */
    public Long getLikeCount(Long bizId) {
        // 熔断器检查
        if (!circuitBreaker.allowRequest()) {
            log.warn("熔断器开启，降级到数据库查询, bizId: {}", bizId);
            return getFromDatabaseWithDirtyCheck(bizId);
        }
        
        try {
            Long count = (Long) redisTemplate.opsForValue().get("likeCount:" + bizId);
            // 2、如果缓存中存在就直接返回（包括null值缓存）
            if (count != null) {
                circuitBreaker.recordSuccess();
                return NULL_CACHE_VALUE.equals(count) ? 0L : count;
            }
            
            // 3、缓存不存在时，先检查是否正在同步（关键优化）
            String cacheKey = "likeCount:" + bizId;
            RLock syncLock = redissonClient.getLock("expire-sync:" + cacheKey);
            
            // 如果正在同步，等待同步完成后再查缓存
            if (syncLock.isLocked()) {
                try {
                    // 等待同步完成，最多等待1秒
                    if (syncLock.tryLock(1, 5, TimeUnit.SECONDS)) {
                        syncLock.unlock();
                    }
                    // 同步完成后再次尝试从缓存获取
                    count = (Long) redisTemplate.opsForValue().get(cacheKey);
                    if (count != null) {
                        return NULL_CACHE_VALUE.equals(count) ? 0L : count;
                    }
                } catch (InterruptedException e) {
                    log.warn("等待同步完成被中断, bizId: {}", bizId);
                }
            }
            
            // 使用更精确的锁key，减少锁冲突
            String lockKey = "rebuild-cache:" + (bizId % 1000); // 按业务ID取模分组
            RLock lock = redissonClient.getLock(lockKey);
            try {
                // 减少锁等待时间，提高并发性能
                if (lock.tryLock(500, 5, TimeUnit.MILLISECONDS)) {
                    // 成功获取到锁, 再次进行检查获取数据
                    count = (Long) redisTemplate.opsForValue().get(cacheKey);
                    if (count != null) {
                        return NULL_CACHE_VALUE.equals(count) ? 0L : count;
                    }
                    
                    // 关键优化：先从对应分片的脏数据队列获取最新值
                    String dirtyQueueKey = getDirtyQueueKey(bizId);
                    Double dirtyValue = redisTemplate.opsForZSet().score(dirtyQueueKey, cacheKey);
                    if (dirtyValue != null) {
                        // 如果脏数据队列中有数据，说明正在同步中，使用脏数据的值
                        long latestCount = dirtyValue.longValue();
                        redisTemplate.opsForValue().set(cacheKey, latestCount, CACHE_EXPIRE);
                        log.debug("使用脏数据队列中的最新值重建缓存: {}, 值: {}", bizId, latestCount);
                        return latestCount;
                    }
                    
                    // 开始进行数据库查询工作
                    BmLikeCount likeCount = this.lambdaQuery().eq(BmLikeCount::getBizId, bizId).one();
                    // 优化：对null值也进行缓存，防止缓存穿透
                    if (likeCount == null) {
                        redisTemplate.opsForValue().set(cacheKey, NULL_CACHE_VALUE, NULL_CACHE_EXPIRE);
                        return 0L;
                    } else {
                        redisTemplate.opsForValue().set(cacheKey, likeCount.getLikeNum(), CACHE_EXPIRE);
                        return likeCount.getLikeNum();
                    }
                } else {
                    // 优化：锁获取失败时降级到直接查数据库
                    log.warn("获取重建缓存锁失败，降级到直接查数据库, bizId: {}", bizId);
                    return getFromDatabaseWithDirtyCheck(bizId);
                }
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }


        } catch (Exception e) {
            log.error("获取点赞数异常，降级到数据库查询, bizId: {}", bizId, e);
            circuitBreaker.recordFailure();
            return getFromDatabaseWithDirtyCheck(bizId);
        }
    }
    
    /**
     * 从数据库获取点赞数，优先检查脏数据队列（降级方案）
     */
    private Long getFromDatabaseWithDirtyCheck(Long bizId) {
        try {
            String cacheKey = "likeCount:" + bizId;
            String dirtyQueueKey = getDirtyQueueKey(bizId);
            
            // 优先检查对应分片的脏数据队列
            Double dirtyValue = redisTemplate.opsForZSet().score(dirtyQueueKey, cacheKey);
            if (dirtyValue != null) {
                log.debug("从脏数据队列获取数据: {}, 值: {}", bizId, dirtyValue.longValue());
                return dirtyValue.longValue();
            }
            
            // 数据库查询
            BmLikeCount likeCount = this.lambdaQuery().eq(BmLikeCount::getBizId, bizId).one();
            return likeCount == null ? 0L : likeCount.getLikeNum();
        } catch (Exception e) {
            log.error("数据库查询失败, bizId: {}", bizId, e);
            return 0L;
        }
    }
    
    /**
     * 热点数据预热（仅主实例执行）
     */
    @Scheduled(cron = "0 0 */6 * * ?") // 每6小时预热一次
    @Async
    public void preWarmHotData() {
        if (!isMaster) {
            return;
        }
        
        try {
            // 查询热点业务ID（这里简单模拟，实际可从日志分析或业务接口获取）
            List<Long> hotBizIds = getHotBizIds();
            
            hotBizIds.parallelStream().forEach(bizId -> {
                try {
                    String cacheKey = "likeCount:" + bizId;
                    if (!redisTemplate.hasKey(cacheKey)) {
                        Long count = getFromDatabaseWithDirtyCheck(bizId);
                        redisTemplate.opsForValue().set(cacheKey, count, CACHE_EXPIRE);
                        log.debug("预热缓存: {}, 值: {}", bizId, count);
                    }
                } catch (Exception e) {
                    log.warn("预热缓存失败: {}", bizId, e);
                }
            });
            
            log.info("热点数据预热完成，预热了 {} 条数据", hotBizIds.size());
        } catch (Exception e) {
            log.error("热点数据预热异常", e);
        }
    }
    
    /**
     * 获取热点业务ID列表（模拟实现）
     */
    private List<Long> getHotBizIds() {
        // 这里可以从缓存分析、日志统计或业务接口获取热点数据
        // 暂时返回空列表，实际使用时需要根据业务需求实现
        return new ArrayList<>();
    }
    
    /**
     * 定时清理脏数据队列中的过期数据（防止内存泄漏，仅主实例执行）
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    @Async
    public void cleanExpiredDirtyData() {
        if (!isMaster) {
            return;
        }
        
        asyncExecutor.execute(() -> {
            try {
                int totalCleanedCount = 0;
                
                // 清理所有分片的过期数据
                for (int i = 0; i < SHARD_COUNT; i++) {
                    String shardKey = "dirty_likes_shard_" + i;
                    int cleanedCount = cleanShardExpiredData(shardKey);
                    totalCleanedCount += cleanedCount;
                }
                
                if (totalCleanedCount > 0) {
                    log.info("定时清理任务完成，清理了 {} 条过期数据", totalCleanedCount);
                }
            } catch (Exception e) {
                log.error("清理脏数据任务异常", e);
            }
        });
    }
    
    /**
     * 清理单个分片的过期数据
     */
    private int cleanShardExpiredData(String shardKey) {
        try {
            // 获取分片中的所有脏数据
            Set<Object> allDirtyKeys = redisTemplate.opsForZSet().range(shardKey, 0, -1);
            
            if (allDirtyKeys == null || allDirtyKeys.isEmpty()) {
                return 0;
            }
            
            int cleanedCount = 0;
            for (Object keyObj : allDirtyKeys) {
                String key = (String) keyObj;
                // 检查缓存中是否还存在该key
                if (!redisTemplate.hasKey(key)) {
                    // 如果缓存中不存在，说明已经过期且可能没有被处理
                    // 执行最后一次同步操作
                    Double likeCount = redisTemplate.opsForZSet().score(shardKey, key);
                    if (likeCount != null) {
                        try {
                            String[] split = StringUtil.split(key, ":");
                            if (split.length >= 2) {
                                bmLikeMapper.update(Wrappers.lambdaUpdate(BmLikeCount.class)
                                        .eq(BmLikeCount::getBizId, Long.valueOf(split[1]))
                                        .set(BmLikeCount::getLikeNum, likeCount.longValue()));
                                cleanedCount++;
                            }
                        } catch (Exception e) {
                            log.warn("清理过程中同步数据失败: {}", key, e);
                        }
                    }
                    // 从脏数据队列中删除
                    redisTemplate.opsForZSet().remove(shardKey, key);
                }
            }
            return cleanedCount;
        } catch (Exception e) {
            log.error("清理分片数据异常: {}", shardKey, e);
            return 0;
        }
    }

}
