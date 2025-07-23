package com.itsoku.lesson084.article;

import com.itsoku.lesson084.common.BusinessExceptionUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发布次数校验器，每日限制5篇文章
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class PublishCountCheck extends AbstractCheck {
    AtomicInteger publishCount = new AtomicInteger(0);

    @Override
    protected void checkIn(ArticlePublishRequest req) {
        if (publishCount.incrementAndGet() > 5) {
            //发布次数校验，比如每日只允许发布5篇文章
            throw BusinessExceptionUtils.businessException("今日发布已满，请明日继续分享！");
        }
    }
}
