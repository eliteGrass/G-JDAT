package com.liteGrass.ai.langChain.config;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: LLMConfig
 * @Author: liteGrass
 * @Date: 2025/11/21 8:39
 * @Description: 大模型配置类
 */
@Configuration
public class LLMConfig {

    @Bean("qWen")
    public ChatModel qWenChatModel() {
        return OpenAiChatModel.builder()
                .apiKey("sk-d110985f96c84e729494ab88b3ed00c7")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen3-max")
                .build();
    }

    @Bean("deepSeek")
    public ChatModel deepSeekChatModel() {
        return OpenAiChatModel.builder()
                .apiKey("sk-8093068c1d4e4f7ea2baf027ba68df5b")
                .baseUrl("https://api.deepseek.com/v1")
                .modelName("deepseek-chat")
                .build();
    }

}
