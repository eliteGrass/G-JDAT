package com.itsoku.lesson004.concurrencysafe;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/29 15:56 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ConcurrencyFailException extends RuntimeException {
    private String key;

    public ConcurrencyFailException(String message, String key) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
