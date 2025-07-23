package com.itsoku.lesson027.ds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 13:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadWrite {

    /**
     * 获取路由策略（主库、从库、还是强制路由到主库？）
     *
     * @return
     */
    ReadWriteRoutingStrategy value();
}
