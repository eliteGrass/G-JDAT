package com.itsoku.lesson041.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 12:57 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    /**
     * 锁名称
     *
     * @return
     */
    String lockName();

    /**
     * 锁等待时间，必须大于0
     *
     * @return
     */
    long waitTime() default 3;

    /**
     * 锁持有时间（-1：锁会自动续期），可取值：-1或大于0
     *
     * @return
     */
    long leaseTime() default -1;

    /**
     * 时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 上锁失败提示信息
     *
     * @return
     */
    String message() default "业务正在处理中，请勿重复操作";
}
