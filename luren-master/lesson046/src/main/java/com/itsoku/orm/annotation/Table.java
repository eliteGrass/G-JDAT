package com.itsoku.orm.annotation;

import java.lang.annotation.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 19:23 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Table {
    /**
     * 表名
     *
     * @return
     */
    String value();
}
