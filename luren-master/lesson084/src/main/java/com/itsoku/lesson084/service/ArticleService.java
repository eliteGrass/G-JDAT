package com.itsoku.lesson084.service;

import com.itsoku.lesson084.common.BusinessExceptionUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class ArticleService {
    /**
     * 发布文章
     *
     * @param req
     */
    public void publish(ArticlePublishRequest req) {
        //1.长度校验，长度在[1,10000]个字符之间
        this.checkLength(req);

        //2.敏感词校验
        this.checkSensitiveWords(req);

        //3.图片合法性校验
        this.checkImage(req);

        //todo 业务处理
    }

    private void checkLength(ArticlePublishRequest req) {
        if (StringUtils.length(req.getContent()) < 1 || StringUtils.length(req.getContent()) > 10000) {
            throw BusinessExceptionUtils.businessException("文章长度不能超过10000个字符");
        }
    }

    public void checkSensitiveWords(ArticlePublishRequest req) {
        //敏感词列表
        List<String> sensitiveWordsList = Arrays.asList("路人");
        //有敏感词则抛出异常
        for (String sw : sensitiveWordsList) {
            if (req.getContent().contains(sw)) {
                throw BusinessExceptionUtils.businessException("有敏感词：" + sw);
            }
        }
    }

    public void checkImage(ArticlePublishRequest req) {
        //校验图片是否合法？不合法则抛出异常
        boolean checked = false;
        if (!checked) {
            throw BusinessExceptionUtils.businessException("图片不合法");
        }
    }

}
