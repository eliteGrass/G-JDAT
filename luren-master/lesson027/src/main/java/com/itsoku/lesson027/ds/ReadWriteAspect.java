package com.itsoku.lesson027.ds;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/25 13:46 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
public class ReadWriteAspect {

    /**
     * 环绕通知，拦截所有方法上标注有 @ReadWrite注解的方法
     *
     * @param joinPoint
     * @param readWrite
     * @return
     * @throws Throwable
     */
    @Around("@annotation(readWrite)")
    public Object around(ProceedingJoinPoint joinPoint, ReadWrite readWrite) throws Throwable {
        //从ThreadLocal中获取读写策略
        ReadWriteRoutingStrategy readWriteRoutingStrategy = ReadWriteRoutingStrategyHolder.getReadWriteRoutingStrategy();
        // 若选择了强制路由到主库，则执行执行业务
        if (readWriteRoutingStrategy == ReadWriteRoutingStrategy.HIT_MASTER) {
            return joinPoint.proceed();
        }
        // 否则，从@ReadWrite注解中获取读写策略，放到ThreadLocal中，然后去执行业务
        ReadWriteRoutingStrategyHolder.setReadWriteRoutingStrategy(readWrite.value());
        return joinPoint.proceed();
    }
}
