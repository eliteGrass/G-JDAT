package com.ms.dts.business.service.tcc.interceptor;

import com.itsoku.common.ResultDto;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.tcc.branch.TccBranchContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>：事务拦截器后执行（即运行在事务中） <br>
 * <b>time</b>：2024/4/28 15:22 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 5)
@Slf4j
@Component
public class TccBranchStepAfterInterceptor extends TccBranchStepInterceptor {

    public TccBranchStepAfterInterceptor() {
        log.info(this.getClass().getName());
    }

    protected Object aroundIn(final TccBranchStepInterceptorParam tccBranchStepInterceptorParam) throws Throwable {
        ResultDto<TccBranchContext> resultDto;
        try {
            resultDto = this.process(tccBranchStepInterceptorParam.getPjp());
        } catch (Throwable e) {
            //有事务的情况下，如果有异常，将状态置为失败
            if (tccBranchStepInterceptorParam.isAutoComplete()) {
                tccBranchStepInterceptorParam.getTccBranchContext().getResponse().setResultStatus(ResultStatus.FAIL);
                tccBranchStepInterceptorParam.getTccBranchContext().getResponse().setMsg(e.getMessage());
            }
            throw e;
        }
        tccBranchStepInterceptorParam.setResultDto(resultDto);
        if (tccBranchStepInterceptorParam.isAutoComplete()) {
            this.updateCurTccBranchResponseCallAfter(tccBranchStepInterceptorParam, ResultStatus.SUCCESS);
            this.updateTccBusBranchLogModelStatus(tccBranchStepInterceptorParam);
        }
        return resultDto;
    }

}
