package com.itsoku.lesson042.service1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 16:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */

@FeignClient(name = "com.itsoku.lesson042.service1.Service2FeignClient",
        url = "http://localhost:8082")
public interface Service2FeignClient {
    @GetMapping("/service2/test1")
    String test1(@RequestParam("callType") String callType);
}
