package com.itsoku.lesson052;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/26 20:19 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        return "优雅停机";
    }
}
