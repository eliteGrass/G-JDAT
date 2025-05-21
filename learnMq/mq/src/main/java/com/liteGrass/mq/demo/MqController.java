package com.liteGrass.mq.demo;


import com.liteGrass.mq.demo.delay.SpringBootDelayProduct;
import com.liteGrass.mq.demo.normal.SpringBootNormalProduct;
import com.liteGrass.mq.demo.order.SpringBootOrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * @Description 控制层
 * @Author liteGrass
 * @Date 2024/12/6 9:25
 */
@RestController
@RequestMapping("/mq")
@RequiredArgsConstructor
public class MqController {

    private final SpringBootNormalProduct springBootNormalProduct;
    private final SpringBootOrderProduct springBootOrderProduct;
    private final SpringBootDelayProduct springBootDelayProduct;

    @GetMapping("/asyncSendNormalMessage")
    public String asyncSendNormalMessage() throws InterruptedException {
        // 处理业务消息
        System.out.println("业务消息处理完成");
        Thread.sleep(Duration.ofSeconds(3));
        //处理完成后发送mq
        springBootNormalProduct.syncSendNormalMessage();
        return "异步消息处理完成";
    }

    @GetMapping("/syncSendOrderMessage")
    public String syncSendOrderMessage() throws InterruptedException {
        // 处理业务消息
        System.out.println("业务消息处理完成");
        Thread.sleep(Duration.ofSeconds(3));
        //处理完成后发送mq
        springBootOrderProduct.syncSendOrderMessage();
        return "顺序消息处理完成";
    }

    @GetMapping("/syncSendDelayMessage")
    public String syncSendDelayMessage() throws InterruptedException {
        // 处理业务消息
        System.out.println("业务消息处理完成");
        Thread.sleep(Duration.ofSeconds(3));
        //处理完成后发送mq
        springBootDelayProduct.syncSendDelayMessage();
        return "顺序消息处理完成";
    }

}
