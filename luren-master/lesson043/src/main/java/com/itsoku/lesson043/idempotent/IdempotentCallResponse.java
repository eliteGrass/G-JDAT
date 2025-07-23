package com.itsoku.lesson043.idempotent;

import lombok.*;

/**
 * 幂等调用响应结果
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class IdempotentCallResponse<T> {
    /**
     * 状态，0：处理中，1：处理成功，-1：处理失败
     */
    private Integer status;
    /**
     * 当status为失败时，这里可以是更详细的错误编码
     */
    private String code;

    /**
     * 当status为失败时，这里可以是更详细的错误提示信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public static <T> IdempotentCallResponse<T> success(T data) {
        IdempotentCallResponse<T> response = new IdempotentCallResponse<>();
        response.setStatus(IdempotentCallStatusEnum.success.getValue());
        response.setData(data);
        return response;
    }

    public static <T> IdempotentCallResponse<T> fail(String code, String message) {
        IdempotentCallResponse<T> response = new IdempotentCallResponse<>();
        response.setStatus(IdempotentCallStatusEnum.fail.getValue());
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> IdempotentCallResponse<T> fail(String message) {
        IdempotentCallResponse<T> response = new IdempotentCallResponse<>();
        response.setStatus(IdempotentCallStatusEnum.fail.getValue());
        response.setMessage(message);
        return response;
    }
}
