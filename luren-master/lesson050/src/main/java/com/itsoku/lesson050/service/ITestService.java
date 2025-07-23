package com.itsoku.lesson050.service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/14 19:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITestService {
    /**
     * 普通插入，在一个事务中
     */
    void singleThreadInsert();

    /**
     * 多线程事务批量插入
     */
    void moreThreadInsert();

    /**
     * 多线程事务批量插入，模拟失败效果
     */
    void moreThreadInsertFail();

    void delete();
}
