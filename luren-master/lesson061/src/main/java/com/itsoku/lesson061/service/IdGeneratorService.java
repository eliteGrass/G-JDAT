package com.itsoku.lesson061.service;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/6 13:57 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IdGeneratorService {

    /**
     * 获取一个id
     *
     * @param code 业务编码
     * @return
     */
    Long getId(String code);

    /**
     * 批量获取一个id
     *
     * @param code 业务编码
     * @param num  获取的数量
     * @return
     */
    List<Long> getIdList(String code, int num);
}
