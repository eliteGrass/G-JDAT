package com.itsoku.lesson044.service2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 15:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class Service2Controller {
    /**
     * @param callType 调用方式，为了方便查看日志
     * @return
     */
    @GetMapping("/service2/test1")
    public String test1(@RequestParam("callType") String callType) {
        log.info("callType:{}", callType);
        return "我是service2";
    }
}
