package com.itsoku.lesson041.common;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:38 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class BusinessException extends RuntimeException {
    private String code;

    /**
     * @param code    错误编码
     * @param message 错误提示
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
