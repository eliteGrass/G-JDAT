package com.itsoku.lesson027.ds;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum ReadWriteRoutingStrategy {
    MASTER, //路由到主库
    SLAVE, //路由到从库
    HIT_MASTER //强制路由到主库
}
