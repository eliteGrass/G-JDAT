package com.itsoku.lesson034.mq;

import com.itsoku.lesson034.lock.DistributeLock;
import com.itsoku.lesson034.mq.consumer.retry.DefaultMsgConsumeRetry;
import com.itsoku.lesson034.mq.consumer.retry.MsgConsumeRetry;
import com.itsoku.lesson034.mq.delay.DelayTaskProcessor;
import com.itsoku.lesson034.mq.mapper.MsgMapper;
import com.itsoku.lesson034.mq.properties.MqProperties;
import com.itsoku.lesson034.mq.sender.DefaultMsgSender;
import com.itsoku.lesson034.mq.sender.IMsgSender;
import com.itsoku.lesson034.mq.sender.MqSendRetryJob;
import com.itsoku.lesson034.mq.sender.retry.DefaultMqSendRetry;
import com.itsoku.lesson034.mq.sender.retry.MqSendRetry;
import com.itsoku.lesson034.mq.service.IMsgService;
import com.itsoku.lesson034.mq.service.ISequentialMsgNumberGeneratorService;
import com.itsoku.lesson034.mq.service.impl.MsgServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@ComponentScan
@MapperScan(basePackageClasses = MsgMapper.class)
@EnableScheduling
@EnableConfigurationProperties(MqProperties.class)
public class MqConfiguration {

    private MqProperties mqProperties;

    public MqConfiguration(MqProperties mqProperties) {
        this.mqProperties = mqProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MqSendRetry mqSendRetry() {
        return new DefaultMqSendRetry();
    }

    @Bean
    public DelayTaskProcessor delayMsgProcessor() {
        return new DelayTaskProcessor("delayMsgProcessor", this.mqProperties.getDelayMsgDelayQueueCapacity(), Runtime.getRuntime().availableProcessors());
    }

    @Bean
    public DelayTaskProcessor delaySendRetryProcessor() {
        return new DelayTaskProcessor("delaySendRetryProcessor", this.mqProperties.getDelaySendRetryDelayQueueCapacity(), Runtime.getRuntime().availableProcessors());
    }

    @Bean
    @ConditionalOnMissingBean
    public IMsgSender msgSender(IMsgService msgService, MqSendRetry mqSendRetry, DistributeLock distributeLock, RabbitTemplate rabbitTemplate, ISequentialMsgNumberGeneratorService sequentialMsgNumberGeneratorService, TransactionTemplate transactionTemplate) {
        return new DefaultMsgSender(this.mqProperties,
                msgService,
                mqSendRetry,
                this.mqExecutor(),
                this.delayMsgProcessor(),
                this.delaySendRetryProcessor(),
                distributeLock,
                rabbitTemplate,
                sequentialMsgNumberGeneratorService,
                transactionTemplate);
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

    @Bean
    @ConditionalOnMissingBean
    public MsgConsumeRetry msgConsumeRetry() {
        return new DefaultMsgConsumeRetry();
    }

}
