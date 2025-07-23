package com.itsoku.lesson043.idempotent;

/**
 * 幂等调用
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IdempotentCall<I, O> {
    /**
     * 幂等方法（支持重复调用）
     *
     * @param request
     * @return
     */
    IdempotentCallResponse<O> call(IdempotentCallRequest<I> request);
}
