package com.itsoku.orm.annotation;

import com.itsoku.orm.IdType;

import java.lang.annotation.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 19:26 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableId {
    IdType type() default IdType.NONE;
}
