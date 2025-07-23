package com.ms.dts.business.service.tcc.interceptor;

import com.itsoku.common.ResultDto;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.tcc.branch.ExceptionDto;
import com.ms.dts.tcc.branch.ITccBranchBus;
import com.ms.dts.tcc.branch.TccBranchContext;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;
import lombok.*;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <b>description</b>：当前步骤拦截器中数据封装 <br>
 * <b>time</b>：2024/4/29 14:27 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TccBranchStepInterceptorParam {
    private ProceedingJoinPoint pjp;
    private ITccBranchBus tccBranchBus;
    private TccBranchContext tccBranchContext;
    private boolean autoComplete;
    private String classname;
    private TccBranchMethodEnums tccBranchMethodEnums;
    private String methodKey;
    //当前步骤响应结果
    private ResultDto<TccBranchContext> resultDto;
    //当前步骤日子
    private TccBusBranchLogPO tccBusBranchLogPO;
    //异常
    private Throwable error;
    //异常信息
    private ExceptionDto exceptionDto;
}
