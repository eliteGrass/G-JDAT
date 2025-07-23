package com.itsoku.lesson009.config;

import com.itsoku.lesson009.comm.ThreadPoolManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人 <br/>
 * <b>time</b>：2024/4/2 15:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration(proxyBeanMethods = false)
public class ThreadPoolConfiguration {
    /**
     * 发送邮件用到的线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor emailThreadPool() {
        return ThreadPoolManager.newThreadPool("emailThreadPool", 10, 20, 1000);
    }

    /**
     * 发送短信用到的线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor smsThreadPool() {
        return ThreadPoolManager.newThreadPool("smsThreadPool");
    }
}
