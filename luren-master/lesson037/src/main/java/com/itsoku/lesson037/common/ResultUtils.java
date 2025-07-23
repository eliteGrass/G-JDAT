package com.itsoku.lesson037.common;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:34 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ResultUtils {
    public static <T> Result<T> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(false, null, null, msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        return new Result<>(false, null, code, msg);
    }
}
