package com.itsoku.lesson027.ds;

import java.util.function.Supplier;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/25 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ReadWriteRoutingStrategyHolder {
    private static ThreadLocal<ReadWriteRoutingStrategy> readWriteRoutingStrategyThreadLocal = new ThreadLocal<>();

    public static void setReadWriteRoutingStrategy(ReadWriteRoutingStrategy readWriteRoutingStrategy) {
        readWriteRoutingStrategyThreadLocal.set(readWriteRoutingStrategy);
    }

    /**
     * 路由到主库
     */
    public static void master() {
        setReadWriteRoutingStrategy(ReadWriteRoutingStrategy.MASTER);
    }

    /**
     * 路由到从库
     */
    public static void slave() {
        setReadWriteRoutingStrategy(ReadWriteRoutingStrategy.SLAVE);
    }

    /**
     * 强制走主库执行 execute的代码
     *
     * @param execute
     * @param <T>
     * @return
     */
    public static <T> T hitMaster(Supplier<T> execute) {
        ReadWriteRoutingStrategy old = getReadWriteRoutingStrategy();
        try {
            setReadWriteRoutingStrategy(ReadWriteRoutingStrategy.HIT_MASTER);
            return execute.get();
        } finally {
            readWriteRoutingStrategyThreadLocal.set(old);
        }
    }

    /**
     * 获取读写策略
     *
     * @return
     */
    public static ReadWriteRoutingStrategy getReadWriteRoutingStrategy() {
        return readWriteRoutingStrategyThreadLocal.get();
    }
}
