package com.itsoku.lesson036.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 14:51 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class RabbitMQConfiguration {

    /**
     * 转账队列配置
     */
    @Configuration
    public static class Transfer {
        public static final String EXCHANGE = "transferExchange";
        public static final String QUEUE = "transferQueue";
        public static final String ROUTING_KEY = "transfer";

        @Bean
        public DirectExchange orderExchange() {
            return new DirectExchange(EXCHANGE, true, false);
        }

        @Bean
        public Queue orderQueue() {
            return new Queue(QUEUE, true);
        }

        @Bean
        public Binding orderExchangeBindOrderQueue() {
            return new Binding(QUEUE, Binding.DestinationType.QUEUE, EXCHANGE, ROUTING_KEY, null);
        }
    }
}
