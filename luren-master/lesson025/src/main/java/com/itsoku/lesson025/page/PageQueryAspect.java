package com.itsoku.lesson025.page;

import com.github.pagehelper.PageHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 21:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PageQueryAspect {

    /**
     * 拦截mapper中的素有方法
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.itsoku..*Mapper.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        boolean pageFlag = false;
        try {
            //遍历参数，参数中如果有 IPageQuery 类型的，则从 IPageQuery 取出分页信息，则使用 PageHelper 开启分页
            Object[] args = pjp.getArgs();
            for (Object arg : args) {
                if (arg instanceof IPageQuery) {
                    IPageQuery pageQuery = (IPageQuery) arg;
                    PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.count());
                    pageFlag = true;
                    break;
                }
            }
            return pjp.proceed();
        } finally {
            if (pageFlag) {
                //清理分页信息
                PageHelper.clearPage();
            }
        }
    }
}
