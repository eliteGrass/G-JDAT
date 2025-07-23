package com.itsoku.lesson007.aspect;

import com.itsoku.lesson007.excel.ExcelExportResponse;
import com.itsoku.lesson007.excel.ExcelExportUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人 <br/>
 * <b>time</b>：2024/4/1 19:54 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Aspect
public class ExcelExportAspect {
    @Around(value = "execution(* com.itsoku..*Controller.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        if (result instanceof ExcelExportResponse) {
            //下载excel
            ExcelExportUtils.writeExcelToResponse((ExcelExportResponse) result);
            return null;
        } else {
            return result;
        }
    }
}
