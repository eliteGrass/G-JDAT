package com.ms.dts.business.service.tcc.interceptor;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.common.ResultDto;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.tcc.branch.*;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;
import com.ms.dts.tcc.util.TccUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <b>description</b>：spring事务拦截器执行之前运行，根据tid查看当前分支是否执行过 <br>
 * <b>time</b>：2024/4/28 15:22 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 20)
@Slf4j
@Component
public class TccBranchStepBeforeInterceptor extends TccBranchStepInterceptor {

    public TccBranchStepBeforeInterceptor() {
        log.info(this.getClass().getName());
    }

    protected Object aroundIn(TccBranchStepInterceptorParam tccBranchStepInterceptorParam) throws Throwable {
        TccBusBranchLogPO tccBusBranchLogPO = this.initTccBusBranchLogModel(tccBranchStepInterceptorParam);
        //状态成功 || (方法为try && try执行失败)，直接从日志中拿结果返回
        if (tccBusBranchLogPO.getStatus().equals(ResultStatus.SUCCESS.getStatus()) ||
                (tccBusBranchLogPO.getStatus().equals(ResultStatus.FAIL.getStatus()) && tccBranchStepInterceptorParam.getTccBranchMethodEnums() == TccBranchMethodEnums.try1)) {
            ITccBranchBus tccBranchBus = tccBranchStepInterceptorParam.getTccBranchBus();
            String context = tccBusBranchLogPO.getContext();
            TypeReference<ResultDto<TccBranchContext<TccBranchRequest>>> tccBranchContextTypeReference = tccBranchBus.getTccBranchContextTypeReference();
            ResultDto<TccBranchContext<TccBranchRequest>> resultDto = JSONUtil.toBean(context, tccBranchContextTypeReference, true);
            if (Objects.nonNull(resultDto) && Objects.nonNull(resultDto.getSuccessData())) {
                resultDto.getSuccessData().setTccBusBranchLogModel(tccBusBranchLogPO);
            }
            return resultDto;
        }
        try {
            ResultDto<TccBranchContext> resultDto = this.process(tccBranchStepInterceptorParam.getPjp());
            tccBranchStepInterceptorParam.setResultDto(resultDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            this.callAfterThrowing(tccBranchStepInterceptorParam, e);
        } finally {
            this.callAfter(tccBranchStepInterceptorParam);
        }
        return tccBranchStepInterceptorParam.getResultDto();
    }

    /**
     * 处理结果
     *
     * @param tccBranchStepInterceptorParam
     * @throws Exception
     */
    private void callAfter(TccBranchStepInterceptorParam tccBranchStepInterceptorParam){
        try {
            this.disposeResultDtoCallAfter(tccBranchStepInterceptorParam);
            this.disposeCurTccBranchCallAfter(tccBranchStepInterceptorParam);
            this.updateLogModelCallAfter(tccBranchStepInterceptorParam);
        } finally {
            tccBranchStepInterceptorParam.getTccBranchContext().setTccBusBranchLogModel(tccBranchStepInterceptorParam.getTccBusBranchLogPO());
        }
    }

    private void disposeCurTccBranchCallAfter(TccBranchStepInterceptorParam tccBranchStepInterceptorParam){
        ResultDto<TccBranchContext> resultDto = tccBranchStepInterceptorParam.getResultDto();
        //若不是成功，更新为失败
        if (tccBranchStepInterceptorParam.isAutoComplete() && ResultStatus.INIT.getStatus().equals(tccBranchStepInterceptorParam.getTccBusBranchLogPO().getStatus())) {
            this.updateCurTccBranchResponseCallAfter(tccBranchStepInterceptorParam, ResultStatus.FAIL);
        } else {
            this.updateCurTccBranchResponseCallAfter(tccBranchStepInterceptorParam, resultDto.getSuccessData().getResponse().getResultStatus());
        }
    }

    /**
     * 业务方法执行完毕之后处理LogModel
     *
     * @param tccBranchStepInterceptorParam
     * @throws Exception
     */
    private void updateLogModelCallAfter(TccBranchStepInterceptorParam tccBranchStepInterceptorParam){
        this.updateTccBusBranchLogModelStatus(tccBranchStepInterceptorParam);
    }

    /**
     * 异常处理
     *
     * @param tccBranchStepInterceptorParam
     * @param e
     */
    private void callAfterThrowing(TccBranchStepInterceptorParam tccBranchStepInterceptorParam, Throwable e) {
        tccBranchStepInterceptorParam.setError(e);
        tccBranchStepInterceptorParam.setExceptionDto(TccUtils.getExceptionDto(tccBranchStepInterceptorParam.getError()));
        tccBranchStepInterceptorParam.getTccBranchContext().setExceptionDto(tccBranchStepInterceptorParam.getExceptionDto());
    }

    /**
     * 业务方法调用完毕之后处理结果
     *
     * @param tccBranchStepInterceptorParam
     */
    private void disposeResultDtoCallAfter(TccBranchStepInterceptorParam tccBranchStepInterceptorParam) {
        ResultDto<TccBranchContext> resultDto = tccBranchStepInterceptorParam.getResultDto();
        if (resultDto == null) {
            resultDto = ResultUtils.successData(tccBranchStepInterceptorParam.getTccBranchContext());
            tccBranchStepInterceptorParam.setResultDto(resultDto);
        } else {
            resultDto.setState(ResultUtils.SUCCESS_CODE);
        }
    }

    /**
     * 初始化TccBusBranchLogModel
     *
     * @param tccBranchStepInterceptorParam
     * @return
     * @throws Exception
     */
    private TccBusBranchLogPO initTccBusBranchLogModel(TccBranchStepInterceptorParam tccBranchStepInterceptorParam){
        TccBusBranchLogPO tccBusBranchLogPO;
        ITccBranchRequest request = tccBranchStepInterceptorParam.getTccBranchContext().getRequest();
        String tid = request.getTid();
        if (StringUtils.isBlank(tid)) {
            FrameUtils.throwBaseException("tid不能为空!");
        }
        String classname = tccBranchStepInterceptorParam.getClassname();
        TccBranchMethodEnums tccBranchMethodEnums = tccBranchStepInterceptorParam.getTccBranchMethodEnums();
        tccBusBranchLogPO = this.tccBusBranchLogService.get(tid, classname, tccBranchMethodEnums.getValue());
        if (tccBusBranchLogPO == null) {
            tccBusBranchLogPO = TccBusBranchLogPO.builder().
                    id(IdUtil.fastSimpleUUID()).
                    tccRecordId(tid).
                    classname(classname).
                    method(tccBranchMethodEnums.getValue()).
                    status(ResultStatus.INIT.getStatus()).
                    context(FrameUtils.json(ResultUtils.successData(tccBranchStepInterceptorParam.getTccBranchContext()))).
                    addtime(FrameUtils.getTime()).
                    uptime(0L).
                    version(0L).
                    build();
            this.tccBusBranchLogService.save(tccBusBranchLogPO);
        }
        tccBranchStepInterceptorParam.setTccBusBranchLogPO(tccBusBranchLogPO);
        return tccBusBranchLogPO;
    }

}
