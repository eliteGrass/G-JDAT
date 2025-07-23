package com.itsoku.lesson024.log;

import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 14:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class ThreadLocalUserNameProvider implements IUserNameProvider {
    private ThreadLocal<String> userNameTl = new ThreadLocal<>();

    @Override
    public String getUserName() {
        return this.userNameTl.get();
    }

    @Override
    public void setUserName(String userName) {
        this.userNameTl.set(userName);
    }
}
