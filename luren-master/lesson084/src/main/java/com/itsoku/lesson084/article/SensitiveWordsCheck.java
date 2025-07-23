package com.itsoku.lesson084.article;

import com.itsoku.lesson084.common.BusinessExceptionUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;

import java.util.Arrays;
import java.util.List;

/**
 * 敏感词校验
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SensitiveWordsCheck extends AbstractCheck {

    @Override
    protected void checkIn(ArticlePublishRequest req) {
        //敏感词列表
        List<String> sensitiveWordsList = Arrays.asList("路人");
        //有敏感词则抛出异常
        for (String sw : sensitiveWordsList) {
            if (req.getContent().contains(sw)) {
                throw BusinessExceptionUtils.businessException("有敏感词：" + sw);
            }
        }
    }
}
