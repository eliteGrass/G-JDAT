package com.itsoku.lesson009.comm;

/**
 * <b>description</b>：线程池扩缩容请求参数 <br>
 * <b>time</b>：2024/4/3 7:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ThreadPoolChange {
    //线程池名称
    private String name;
    //核心线程数
    private int corePoolSize;
    //最大线程数
    private int maxPoolSize;
    //队列容量
    private int queueCapacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
