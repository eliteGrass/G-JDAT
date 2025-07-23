package com.itsoku.lesson017.trace;

import com.itsoku.lesson017.common.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * 在返回值中填充traceId，用于方便排查错误
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/24 11:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Order
public class ResultTraceIdAspect {
    @Pointcut("execution(* com.itsoku..*Controller.*(..)) ||execution(* com.itsoku.lesson017.web.GlobalExceptionHandler.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object object = pjp.proceed();
        if (object instanceof Result) {
            ((Result<?>) object).setTraceId(TraceUtils.getTraceId());
        }
        return object;
    }

}
