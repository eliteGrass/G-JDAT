package com.itsoku.lesson030.mq;

import com.itsoku.lesson030.mq.mapper.MsgMapper;
import com.itsoku.lesson030.mq.retry.DefaultMqSendRetry;
import com.itsoku.lesson030.mq.retry.MqSendRetry;
import com.itsoku.lesson030.mq.sender.DefaultMsgSender;
import com.itsoku.lesson030.mq.sender.IMsgSender;
import com.itsoku.lesson030.mq.sender.MqSendRetryJob;
import com.itsoku.lesson030.mq.service.IMsgService;
import com.itsoku.lesson030.mq.service.MsgServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@MapperScan(basePackageClasses = MsgMapper.class)
@EnableScheduling
public class MqConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MqSendRetry mqSendRetry() {
        return new DefaultMqSendRetry();
    }

    @Bean
    @ConditionalOnMissingBean
    public IMsgSender msgSender(IMsgService msgService, MqSendRetry mqSendRetry) {
        return new DefaultMsgSender(msgService, mqSendRetry, this.mqExecutor());
    }

    @Bean
    public IMsgService msgService() {
        return new MsgServiceImpl();
    }

    @Bean
    public MqSendRetryJob mqSendRetryJob(IMsgService msgService, IMsgSender msgSender) {
        return new MqSendRetryJob(msgService, msgSender);
    }

    @Bean
    public ThreadPoolTaskExecutor mqExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("ThreadPool-mqExecutor-");
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 4);
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 8);
        threadPoolTaskExecutor.setQueueCapacity(10000);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
