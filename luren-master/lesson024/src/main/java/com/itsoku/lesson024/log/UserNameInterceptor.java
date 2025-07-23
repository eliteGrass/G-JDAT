package com.itsoku.lesson024.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 15:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class UserNameInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserNameProvider userNameProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //这里从请求中得到用户名，然后塞到userNameProvider中
        String userName = "路人";
        this.userNameProvider.setUserName(userName);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
