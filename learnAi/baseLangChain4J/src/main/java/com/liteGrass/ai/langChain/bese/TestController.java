package com.liteGrass.ai.langChain.bese;


import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: HelloWordAi
 * @Author: liteGrass
 * @Date: 2025/11/19 19:20
 * @Description: ai相关测试教程
 */
@Slf4j
@RestController
public class TestController {

    private final ChatModel qWenChatModel;

    private final ChatModel deepSeekChatModel;

    public TestController(
        @Qualifier("qWen") ChatModel qWenChatModel,
        @Qualifier("deepSeek") ChatModel deepSeekChatModel) {
        this.qWenChatModel = qWenChatModel;
        this.deepSeekChatModel = deepSeekChatModel;
    }

    @GetMapping("/qWenChat")
    public String qWenChat(@RequestParam(value = "prompt", defaultValue = "你是谁？") String prompt) {
        String result = qWenChatModel.chat(prompt);
        return result;
    }


    @GetMapping("/deepSeekChat")
    public String deepSeekChat(@RequestParam(value = "prompt", defaultValue = "你是谁？") String prompt) {
        String result = deepSeekChatModel.chat(prompt);
        return result;
    }

}
