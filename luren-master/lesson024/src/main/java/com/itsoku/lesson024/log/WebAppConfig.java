package com.itsoku.lesson024.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 15:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class WebAppConfig implements WebMvcConfigurer {
    @Autowired
    private UserNameInterceptor userNameInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userNameInterceptor).addPathPatterns("/**");
    }
}
