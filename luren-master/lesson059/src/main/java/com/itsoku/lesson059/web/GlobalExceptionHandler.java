package com.itsoku.lesson059.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/4 20:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    public String handleBusinessException(UnsupportedOperationException e, HttpServletRequest request) {
        log.info("请求：{}，发生异常：{}", request.getRequestURL(), e.getMessage(), e);
        return e.getMessage();
    }
}
