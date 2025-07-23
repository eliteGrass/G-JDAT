package com.itsoku.lesson009.comm;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人 <br/>
 * <b>time</b>：2024/4/2 14:48 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ThreadPoolInfo {
    private String name;
    //核心线程数
    private int corePoolSize;
    //最大线程数
    private int maxPoolSize;
    //活动的线程数
    private int activeCount;
    //队列的容量
    private int queueCapacity;
    //队列中当前任务数量
    private int queueSize;


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

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
