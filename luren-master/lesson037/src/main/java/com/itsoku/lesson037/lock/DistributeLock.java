package com.itsoku.lesson037.lock;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/4 21:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface DistributeLock {
    /**
     * 上锁执行业务操作，上锁失败返回false
     *
     * @param lockKey
     * @param consumer
     * @return
     */
    boolean accept(String lockKey, Consumer<String> consumer);

    /**
     * 上锁执行业务操作
     *
     * @param lockKey
     * @param consumer
     * @param lockFailException 上锁失败将抛出异常
     * @return
     */
    void accept(String lockKey, Consumer<String> consumer, Function<String, ? extends RuntimeException> lockFailException);

}