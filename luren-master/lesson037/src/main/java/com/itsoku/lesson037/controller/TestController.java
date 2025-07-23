package com.itsoku.lesson037.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/18 16:52 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public DeferredResult<String> test() {
        System.out.println(Thread.currentThread());
        DeferredResult<String> deferredResult = new DeferredResult<>(2000000L, () -> {
            System.out.println(Thread.currentThread());
            return "timeout";
        });
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            deferredResult.setResult("ok");
        }).start();
        return deferredResult;
    }
}
