package com.itsoku.orm;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 21:14 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum IdType {
    /**
     * 数据库ID自增
     * <p>该类型请确保数据库设置了 ID自增 否则无效</p>
     */
    AUTO,
    /**
     * 该类型为未设置主键类型
     */
    NONE;
}
