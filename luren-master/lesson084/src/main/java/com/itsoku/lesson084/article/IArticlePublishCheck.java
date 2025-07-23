package com.itsoku.lesson084.article;

import com.itsoku.lesson084.dto.ArticlePublishRequest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IArticlePublishCheck {
    /**
     * 对文章进行校验
     *
     * @param req
     */
    void check(ArticlePublishRequest req);

    /**
     * 设置下一个文章校验器，并返回下一个校验器
     *
     * @param next
     * @return
     */
    IArticlePublishCheck setNext(IArticlePublishCheck next);
}
