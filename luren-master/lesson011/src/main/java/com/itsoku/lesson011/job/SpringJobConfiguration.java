package com.itsoku.lesson011.job;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * SpringJob配置类
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/3 0:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class SpringJobConfiguration {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        //线程池大小
        threadPoolTaskScheduler.setPoolSize(100);
        //线程名称前缀
        threadPoolTaskScheduler.setThreadNamePrefix("taskExecutor-");
        //等待时长
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        //关闭任务线程时是否等待当前被调度的任务完成
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskScheduler;
    }
}
