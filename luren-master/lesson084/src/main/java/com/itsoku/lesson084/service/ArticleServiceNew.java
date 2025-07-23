package com.itsoku.lesson084.service;

import com.itsoku.lesson084.article.IArticlePublishCheck;
import com.itsoku.lesson084.dto.ArticlePublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class ArticleServiceNew {

    @Autowired
    private IArticlePublishCheck articlePublishCheck;

    /**
     * 发布文章
     *
     * @param req
     */
    public void publish(ArticlePublishRequest req) {
        //1.文章合法性校验
        this.articlePublishCheck.check(req);

        //todo 业务处理
    }
}
