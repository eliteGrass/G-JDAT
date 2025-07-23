package com.itsoku.lesson084.article;

import com.itsoku.lesson084.dto.ArticlePublishRequest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public abstract class AbstractCheck implements IArticlePublishCheck {

    private IArticlePublishCheck next;

    @Override
    public void check(ArticlePublishRequest req) {
        //校验
        this.checkIn(req);
        //调用下一个校验器进行校验
        if (this.next != null) {
            this.next.check(req);
        }
    }

    /**
     * 子类实现
     *
     * @param req
     */
    protected abstract void checkIn(ArticlePublishRequest req);

    @Override
    public IArticlePublishCheck setNext(IArticlePublishCheck next) {
        this.next = next;
        return this.next;
    }
}
