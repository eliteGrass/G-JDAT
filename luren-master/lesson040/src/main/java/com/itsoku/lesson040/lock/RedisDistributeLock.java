package com.itsoku.lesson040.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/23 22:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class RedisDistributeLock implements DistributeLock {

    private Logger logger = LoggerFactory.getLogger(RedisDistributeLock.class);
    private RedissonClient redissonClient;
    private DistributeLockProperties distributeLockProperties;

    public RedisDistributeLock(RedissonClient redisson, DistributeLockProperties distributeLockProperties) {
        this.redissonClient = redisson;
        this.distributeLockProperties = distributeLockProperties;
    }

    public boolean tryLockRun(String lockKey, Runnable runnable) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(runnable);
        String redisKey = this.getLockKey(lockKey);
        RLock lock = redissonClient.getLock(redisKey);
        boolean flag = false;
        try {
            flag = lock.tryLock();
            if (logger.isDebugEnabled()) {
                logger.debug("get lock:[{}],[{}]", redisKey, flag);
            }
            if (!flag) {
                return false;
            }
            runnable.run();
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
        return true;
    }

    public boolean tryLockRun(String lockKey, Runnable runnable, long waitTime, TimeUnit unit) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(runnable);
        if (waitTime <= 0) {
            throw new IllegalArgumentException("waitTime > 0");
        }
        String redisKey = this.getLockKey(lockKey);
        RLock lock = redissonClient.getLock(redisKey);
        boolean flag = false;
        try {
            flag = lock.tryLock(waitTime, unit);
            if (logger.isDebugEnabled()) {
                logger.debug("get lock:[{}],[{}]", redisKey, flag);
            }
            if (!flag) {
                return false;
            }
            runnable.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public boolean tryLockRun(String lockKey, Runnable runnable, long waitTime, long leaseTime, TimeUnit unit) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(runnable);
        if (waitTime <= 0) {
            throw new IllegalArgumentException("waitTime > 0");
        }
        if (leaseTime <= 0) {
            throw new IllegalArgumentException("leaseTime > 0");
        }
        String redisKey = this.getLockKey(lockKey);
        RLock lock = redissonClient.getLock(redisKey);
        boolean flag = false;
        try {
            flag = lock.tryLock(waitTime, leaseTime, unit);
            if (logger.isDebugEnabled()) {
                logger.debug("get lock:[{}],[{}]", redisKey, flag);
            }
            if (!flag) {
                return false;
            }
            runnable.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
        return true;
    }

    public void lockRun(String lockKey, Runnable runnable) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(runnable);
        String redisKey = this.getLockKey(lockKey);
        RLock lock = redissonClient.getLock(redisKey);
        lock.lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("get lock:[{}]", redisKey);
            }
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void lockRun(String lockKey, Runnable runnable, long leaseTime, TimeUnit unit) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(runnable);
        if (leaseTime <= 0) {
            throw new IllegalArgumentException("leaseTime > 0");
        }
        String redisKey = this.getLockKey(lockKey);
        RLock lock = redissonClient.getLock(redisKey);
        lock.lock(leaseTime, unit);
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("get lock:[{}]", redisKey);
            }
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    private String getLockKey(String key) {
        if (this.distributeLockProperties.getPrefix() == null) {
            return key;
        }
        return String.format("%s%s", this.distributeLockProperties.getPrefix(), key);
    }
}
