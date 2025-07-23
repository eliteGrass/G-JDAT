package com.itsoku.lesson089.mapper;

import com.itsoku.lesson089.po.ArticlePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ArticleMapper {
    List<ArticlePO> findMyArticles1(@Param("userId") Long userId, @Param("keywords") String keywords);

    List<ArticlePO> findMyArticles2(@Param("userId") Long userId, @Param("keywords") String keywords);

}
