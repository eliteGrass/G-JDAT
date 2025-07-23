package com.itsoku.lesson019.common;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class BusinessExceptionUtils {
    /**
     * 创建 BusinessException
     *
     * @param msg
     * @return
     */
    public static BusinessException businessException(String msg) {
        return new BusinessException(null, msg);
    }

    /**
     * 创建 BusinessException
     *
     * @param code
     * @param msg
     * @return
     */
    public static BusinessException businessException(String code, String msg) {
        return new BusinessException(code, msg);
    }

    /**
     * 创建 BusinessException
     *
     * @param code
     * @param msg
     * @param cause
     * @return
     */
    public static BusinessException businessException(String code, String msg, Throwable cause) {
        return new BusinessException(code, msg, cause);
    }
}
