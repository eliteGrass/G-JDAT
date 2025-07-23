package com.ms.dts.business.service.tcc.interceptor;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.FrameUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.business.service.base.service.ITccBusBranchLogService;
import com.ms.dts.tcc.branch.CurTccBranchResponse;
import com.ms.dts.tcc.branch.ITccBranchBus;
import com.ms.dts.tcc.branch.TccBranchContext;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/28 15:22 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public abstract class TccBranchStepInterceptor {

    @Autowired
    protected ITccBusBranchLogService tccBusBranchLogService;

    protected static ThreadLocal<TccBranchStepInterceptorParam> tccBranchStepInterceptorParamThreadLocal = new ThreadLocal<>();

    @Pointcut("target(com.ms.dts.tcc.branch.ITccBranchBus) && (execution(* com..*.try1(..)) || execution(* com..*.confirm(..)) || execution(* com..*.cancel(..)))")
    public void pointcut() {
    }

    @Around("com.ms.dts.business.service.tcc.interceptor.TccBranchStepInterceptor.pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            TccBranchStepInterceptorParam tccBranchStepInterceptorParam = this.buildTccBranchStepInterceptorParam(pjp);
            return this.aroundIn(tccBranchStepInterceptorParam);
        } finally {
            tccBranchStepInterceptorParamThreadLocal.remove();
        }
    }

    protected abstract Object aroundIn(final TccBranchStepInterceptorParam tccBranchStepInterceptorParam) throws Throwable;


    protected void updateCurTccBranchResponseCallAfter(TccBranchStepInterceptorParam tccBranchStepInterceptorParam, ResultStatus resultStatus) {
        CurTccBranchResponse response = tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse();
        tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().setResultStatus(resultStatus);
        if (StringUtils.isBlank(tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().getMsg())) {
            tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().setMsg(resultStatus.getMsg());
        }
        String msg = tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().getMsg();
        if (tccBranchStepInterceptorParam.getExceptionDto() != null) {
            String msg1 = tccBranchStepInterceptorParam.getExceptionDto().getMsg();
            if (StringUtils.isNotBlank(msg1)) {
                msg = msg1;
            }
        }
        if (StringUtils.isBlank(msg)) {
            msg = resultStatus.getMsg();
        }
        response.setMsg(msg);
    }

    /**
     * 构建 TccBranchStepInterceptorParam
     *
     * @param pjp
     * @return
     */
    protected TccBranchStepInterceptorParam buildTccBranchStepInterceptorParam(ProceedingJoinPoint pjp){
        TccBranchStepInterceptorParam tccBranchStepInterceptorParam = tccBranchStepInterceptorParamThreadLocal.get();
        if (Objects.nonNull(tccBranchStepInterceptorParam)) {
            tccBranchStepInterceptorParam.setPjp(pjp);
            return tccBranchStepInterceptorParam;
        }
        String classname = pjp.getTarget().getClass().getName();
        String methodName = this.getMethodName(pjp);
        String methodKey = String.format("%s.%s", classname, methodName);
        MethodSignature methodSignature = this.getMethodSignature(pjp);
        Method method = ReflectionUtils.findMethod(pjp.getTarget().getClass(),methodName, methodSignature.getMethod().getParameterTypes());
        boolean hasTransaction = Objects.nonNull(method.getAnnotation(Transactional.class));
        TccBranchContext tccBranchContext = (TccBranchContext) pjp.getArgs()[0];
        ITccBranchBus tccBranchBus = (ITccBranchBus) pjp.getTarget();
        CurTccBranchResponse response = new CurTccBranchResponse();
        response.setResultStatus(ResultStatus.INIT);
        response.setTid(tccBranchContext.getRequest().getTid());
        tccBranchContext.setResponse(response);
        tccBranchContext.setTccBusBranchLogModel(null);
        tccBranchStepInterceptorParam = TccBranchStepInterceptorParam.builder().
                pjp(pjp).
                tccBranchBus(tccBranchBus).
                tccBranchContext(tccBranchContext).
                autoComplete(hasTransaction).
                classname(classname).
                tccBranchMethodEnums(TccBranchMethodEnums.getByMethodName(methodName)).
                methodKey(methodKey).
                build();
        tccBranchStepInterceptorParamThreadLocal.set(tccBranchStepInterceptorParam);
        return tccBranchStepInterceptorParam;
    }

    /**
     * 获取方法名称
     *
     * @param pjp
     * @return
     */
    protected String getMethodName(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = this.getMethodSignature(pjp);
        String methodName = methodSignature.getMethod().getName();
        return methodName;
    }

    /**
     * 获取 MethodSignature
     *
     * @param pjp
     * @return
     */
    public MethodSignature getMethodSignature(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        return (MethodSignature) signature;
    }

    protected ResultDto<TccBranchContext> process(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        ResultDto<TccBranchContext> resultDto = (ResultDto<TccBranchContext>) result;
        return resultDto;
    }

    /**
     * 更新 TccBusBranchLogModel 的状态，confirm和cancel不存在失败的情况
     *
     * @param tccBranchStepInterceptorParam
     * @throws Exception
     */
    protected void updateTccBusBranchLogModelStatus(TccBranchStepInterceptorParam tccBranchStepInterceptorParam){
        ResultStatus resultStatus = tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().getResultStatus();
        TccBusBranchLogPO tccBusBranchLogPO = tccBranchStepInterceptorParam.getTccBusBranchLogPO();
        tccBusBranchLogPO.setStatus(resultStatus.getStatus());
        tccBusBranchLogPO.setContext(FrameUtils.json(tccBranchStepInterceptorParam.getResultDto()));
        tccBusBranchLogPO.setMsg(tccBranchStepInterceptorParam.getResultDto().getSuccessData().getResponse().getMsg());
        tccBusBranchLogPO.setUptime(FrameUtils.getTime());
        boolean update = this.tccBusBranchLogService.updateById(tccBusBranchLogPO);
        log.info("update={},logModel={}", update, this.tccBusBranchLogService.getById(tccBusBranchLogPO.getId()));
        if (!update) {
            log.error("TCC日志更新 {} 出错", FrameUtils.json(tccBusBranchLogPO));
            FrameUtils.throwBaseException("TCC日志更新出错啦!");
        }
    }


}
