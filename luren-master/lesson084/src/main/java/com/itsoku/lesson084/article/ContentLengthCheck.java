package com.itsoku.lesson084.article;

import com.itsoku.lesson084.common.BusinessExceptionUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 内容长度校验
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ContentLengthCheck extends AbstractCheck {

    @Override
    protected void checkIn(ArticlePublishRequest req) {
        if (StringUtils.length(req.getContent()) < 1 || StringUtils.length(req.getContent()) > 10000) {
            throw BusinessExceptionUtils.businessException("文章长度不能超过10000个字符");
        }
    }
}
