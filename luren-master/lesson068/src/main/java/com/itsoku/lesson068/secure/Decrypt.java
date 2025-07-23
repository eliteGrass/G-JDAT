package com.itsoku.lesson068.secure;

import java.lang.annotation.*;

/**
 * 接口方法上添加该注解，这表示这个接口的参数是被加密的，进入方法之前，参数会自动被解密
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/3 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {
}
