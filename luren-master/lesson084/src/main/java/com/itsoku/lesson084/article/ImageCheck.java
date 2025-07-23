package com.itsoku.lesson084.article;

import com.itsoku.lesson084.common.BusinessExceptionUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;

/**
 * 图片合法性校验
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ImageCheck extends AbstractCheck {

    @Override
    protected void checkIn(ArticlePublishRequest req) {
        //校验图片是否合法？不合法则抛出异常
        boolean checked = true;
        if (!checked) {
            throw BusinessExceptionUtils.businessException("图片不合法");
        }
    }

}
