package com.itsoku.lesson058;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/3 19:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test() throws InterruptedException {
        log.info("start");
        this.m1();
        this.m2();
        this.m3();
        log.info("end");
        return "ok";
    }

    private void m1() throws InterruptedException {
        //休眠100毫秒
        TimeUnit.MILLISECONDS.sleep(100);
        log.info("m1");
    }

    private void m2() throws InterruptedException {
        //休眠100毫秒
        TimeUnit.MILLISECONDS.sleep(100);
        log.info("m1");
    }

    private void m3() throws InterruptedException {
        //休眠1秒
        TimeUnit.MILLISECONDS.sleep(1000);
        log.info("m1");
    }
}
