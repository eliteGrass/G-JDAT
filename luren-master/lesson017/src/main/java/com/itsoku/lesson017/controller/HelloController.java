package com.itsoku.lesson017.controller;

import com.itsoku.lesson017.common.Result;
import com.itsoku.lesson017.common.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/10 14:44 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class HelloController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public Result<String> hello() throws InterruptedException {
        logger.info("开始执行业务");
        TimeUnit.MILLISECONDS.sleep(500);
        logger.info("业务执行结束");
        return ResultUtils.success("欢迎和路人一起学习《高并发 & 微服务 & 性能调优实战案例100讲》");
    }

    @GetMapping("/exception")
    public Result<String> exception() throws InterruptedException {
        logger.info("开始执行业务");
        //这里模拟了一个错误，10/0，会报错
        System.out.println(10 / 0);
        logger.info("业务执行结束");
        return ResultUtils.success("欢迎和路人一起学习《高并发 & 微服务 & 性能调优实战案例100讲》");
    }
}
