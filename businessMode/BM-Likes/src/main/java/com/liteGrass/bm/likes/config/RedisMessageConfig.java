package com.liteGrass.bm.likes.config;

import com.liteGrass.bm.likes.component.LikeSyncMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @ClassName: RedisMessageConfig
 * @Author: liteGrass
 * @Date: 2025/8/24
 * @Description: Redis消息监听器配置
 */
@Configuration
@RequiredArgsConstructor
public class RedisMessageConfig {
    
    private final LikeSyncMessageListener likeSyncMessageListener;
    
    /**
     * Redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        
        // 添加点赞同步消息监听器
        container.addMessageListener(
            new MessageListenerAdapter(likeSyncMessageListener, "onMessage"),
            new ChannelTopic("like_sync_channel")
        );
        
        return container;
    }
}