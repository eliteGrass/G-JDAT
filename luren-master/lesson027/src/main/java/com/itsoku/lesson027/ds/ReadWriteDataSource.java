package com.itsoku.lesson027.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/25 13:52 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ReadWriteDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        //这里我们从ThreadLocal中获取路由的策略
        return ReadWriteRoutingStrategyHolder.getReadWriteRoutingStrategy();
    }
}
