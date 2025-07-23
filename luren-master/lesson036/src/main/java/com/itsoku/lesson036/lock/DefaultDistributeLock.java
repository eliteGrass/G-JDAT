package com.itsoku.lesson036.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/4 21:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public class DefaultDistributeLock implements DistributeLock {
    private static Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean accept(String lockKey, Consumer<String> consumer) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(consumer);

        ReentrantLock reentrantLock = getReentrantLock(lockKey);
        boolean flag = reentrantLock.tryLock();
        try {
            if (log.isInfoEnabled()) {
                log.info("get lock:[{}],[{}]", lockKey, flag);
            }
            if (!flag) {
                return false;
            }
            consumer.accept(lockKey);
        } finally {
            if (flag) {
                reentrantLock.unlock();
            }
        }
        return true;
    }

    private static ReentrantLock getReentrantLock(String lockKey) {
        ReentrantLock reentrantLock = lockMap.computeIfAbsent(lockKey, (key) -> new ReentrantLock());
        return reentrantLock;
    }

    @Override
    public void accept(String lockKey, Consumer<String> consumer, Function<String, ? extends RuntimeException> lockFailException) {
        Objects.requireNonNull(lockKey);
        Objects.requireNonNull(consumer);
        Objects.requireNonNull(lockFailException);

        boolean flag = this.accept(lockKey, consumer);
        if (!flag) {
            throw lockFailException.apply(lockKey);
        }
    }
}
