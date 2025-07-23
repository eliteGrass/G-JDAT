package com.itsoku.lesson084.article;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class ArticleCheckConfig {
    @Bean
    public ContentLengthCheck contentLengthCheck() {
        return new ContentLengthCheck();
    }

    @Bean
    public SensitiveWordsCheck sensitiveWordsCheck() {
        return new SensitiveWordsCheck();
    }

    @Bean
    public ImageCheck imageCheck() {
        return new ImageCheck();
    }

    @Bean
    public PublishCountCheck publishCountCheck() {
        return new PublishCountCheck();
    }

    @Bean
    public IArticlePublishCheck articlePublishCheck() {
        ContentLengthCheck firstCheck = this.contentLengthCheck();
        firstCheck
                .setNext(this.sensitiveWordsCheck())
                .setNext(this.imageCheck())
                .setNext(this.publishCountCheck())
                ;
        return firstCheck;
    }
}
