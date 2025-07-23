package com.itsoku.web;

import com.itsoku.common.BusinessException;
import com.itsoku.common.ResultDto;
import com.itsoku.common.ResultException;
import com.itsoku.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:44 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResultDto handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.info("请求：{}，发生异常：{}", request.getRequestURL(), e.getMessage(), e);
        return ResultUtils.resultDto(e);
    }

    /**
     * 处理 ResultException
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ResultException.class)
    public ResultDto handleBusinessException(ResultException e, HttpServletRequest request) {
        logger.info("请求：{}，发生异常：{}", request.getRequestURL(), e.getMessage(), e);
        return e.getResultDto();
    }

    /**
     * 处理SpringBoot参数校验异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResultDto handleBindException(BindException e, HttpServletRequest request) {
        logger.info("请求：{}，发生异常：{}", request.getRequestURL(), e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResultUtils.error(message);
    }

    /**
     * 处理其他异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultDto handleException(Exception e, HttpServletRequest request) {
        logger.info("请求：{}，发生异常：{}", request.getRequestURL(), e.getMessage(), e);
        //会返回code为500的一个异常
        return ResultUtils.error("系统异常，请稍后重试");
    }


}
