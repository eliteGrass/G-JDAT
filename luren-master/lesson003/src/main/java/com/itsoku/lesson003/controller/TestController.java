package com.itsoku.lesson003.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/27 21:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class TestController {
    /**
     * 接口中没有任何处理代码，直接返回结果
     *
     * @return
     */
    @GetMapping("/test1")
    public String test1() {
        log.info("test1");
        return "ok";
    }

    /**
     * 接口中休眠100毫秒，用来模拟业务操作
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/test2")
    public String test2() throws InterruptedException {
        //接口中休眠100毫秒，用来模拟业务操作
        TimeUnit.MILLISECONDS.sleep(100);
        return "ok";
    }
}
