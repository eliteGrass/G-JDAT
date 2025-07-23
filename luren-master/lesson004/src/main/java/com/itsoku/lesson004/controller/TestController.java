package com.itsoku.lesson004.controller;

import com.itsoku.lesson004.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/29 13:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/test1")
    public String test1() throws InterruptedException {
        this.goodsService.placeOrder1();
        return "ok";
    }

    @GetMapping("/test2")
    public String test2() throws InterruptedException {
        this.goodsService.placeOrder2();
        return "ok";
    }

    @GetMapping("/test3")
    public String test3() throws InterruptedException {
        this.goodsService.placeOrder3();
        return "ok";
    }

    @GetMapping("/test4")
    public String test4() throws InterruptedException {
        this.goodsService.placeOrder4();
        return "ok";
    }
}
