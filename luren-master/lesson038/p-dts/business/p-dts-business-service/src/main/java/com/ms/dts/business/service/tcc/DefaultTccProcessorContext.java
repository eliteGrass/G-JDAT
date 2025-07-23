package com.ms.dts.business.service.tcc;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.FrameUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.tcc.branch.*;
import com.ms.dts.tcc.dto.TccProcessorRequestDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>description</b>：tcc处理器上下文 <br>
 * <b>time</b>： 2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class DefaultTccProcessorContext<T extends ITccBranchRequest> {
    //tcc上下文
    private TccBranchContext<T> tccBranchContext;
    //事务请求信息
    private TccProcessorRequestDto tccProcessorRequestDto;
    //try结果
    private BranchResult<T> tryBranchResult;
    //confirm结果
    private BranchResult<T> confirmBranchResult;
    //cancel结果
    private BranchResult<T> cancelBranchResult;
    //事务最终处理结果,成功 | 失败 | 处理中
    private ResultStatus resultStatus;
    //finally方法是否执行完毕
    private Boolean finallyOver = Boolean.FALSE;
    //事务是否已结束
    private Boolean finish = Boolean.FALSE;
    //当前步骤结果
    private ResultDto<TccBranchContext<T>> currentStepResultDto;
    //tcc最终结果
    private TccProcessorResponseDto tccProcessorResponseDto;
    //异常清单
    private List<ExceptionDto> exceptionDtoList;

    public TccBranchContext<T> getTccBranchContext() {
        return tccBranchContext;
    }

    public void setTccBranchContext(TccBranchContext<T> tccBranchContext) {
        this.tccBranchContext = tccBranchContext;
    }

    public TccProcessorRequestDto getTccProcessorRequestDto() {
        return tccProcessorRequestDto;
    }

    public void setTccProcessorRequestDto(TccProcessorRequestDto tccProcessorRequestDto) {
        this.tccProcessorRequestDto = tccProcessorRequestDto;
    }

    public BranchResult<T> getTryBranchResult() {
        return tryBranchResult;
    }

    public void setTryBranchResult(BranchResult<T> tryBranchResult) {
        this.tryBranchResult = tryBranchResult;
    }

    public BranchResult<T> getConfirmBranchResult() {
        return confirmBranchResult;
    }

    public void setConfirmBranchResult(BranchResult<T> confirmBranchResult) {
        this.confirmBranchResult = confirmBranchResult;
    }

    public BranchResult<T> getCancelBranchResult() {
        return cancelBranchResult;
    }

    public void setCancelBranchResult(BranchResult<T> cancelBranchResult) {
        this.cancelBranchResult = cancelBranchResult;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Boolean getFinallyOver() {
        return finallyOver;
    }

    public void setFinallyOver(Boolean finallyOver) {
        this.finallyOver = finallyOver;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public ResultDto<TccBranchContext<T>> getCurrentStepResultDto() {
        return currentStepResultDto;
    }

    public void setCurrentStepResultDto(ResultDto<TccBranchContext<T>> currentStepResultDto) {
        this.currentStepResultDto = currentStepResultDto;
    }

    public TccProcessorResponseDto getTccProcessorResponseDto() {
        return tccProcessorResponseDto;
    }

    public void setTccProcessorResponseDto(TccProcessorResponseDto tccProcessorResponseDto) {
        this.tccProcessorResponseDto = tccProcessorResponseDto;
    }

    public List<ExceptionDto> getExceptionDtoList() {
        return exceptionDtoList;
    }

    public void setExceptionDtoList(List<ExceptionDto> exceptionDtoList) {
        this.exceptionDtoList = exceptionDtoList;
    }

    /**
     * 分支结果
     *
     * @param <T>
     */
    public static class BranchResult<T extends ITccBranchRequest> {
        //分支 -> 分支中方法响应结果
        private Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap;
        //分支中方法执行状态
        private ResultStatus resultStatus;
        //分支中方法状态计数器
        private BranchStatusCounter branchStatusCounter;

        public BranchResult() {
        }

        public BranchResult(Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap, ResultStatus resultStatus, BranchStatusCounter branchStatusCounter) {
            this.branchResponseMap = branchResponseMap;
            this.resultStatus = resultStatus;
            this.branchStatusCounter = branchStatusCounter;
        }

        public Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> getBranchResponseMap() {
            return branchResponseMap;
        }

        public void setBranchResponseMap(Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap) {
            this.branchResponseMap = branchResponseMap;
        }

        public ResultStatus getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(ResultStatus resultStatus) {
            this.resultStatus = resultStatus;
        }

        public BranchStatusCounter getBranchStatusCounter() {
            return branchStatusCounter;
        }

        public void setBranchStatusCounter(BranchStatusCounter branchStatusCounter) {
            this.branchStatusCounter = branchStatusCounter;
        }
    }

    /**
     * branch 结果状态计算器
     */
    public static class BranchStatusCounter {
        //成功数量
        private int successCount;
        //失败数量
        private int failCount;
        //处理中的数量(成功或失败之外的其他状态的统称)
        private int initCount;

        public BranchStatusCounter() {
        }

        public BranchStatusCounter(int successCount, int failCount, int initCount) {
            this.successCount = successCount;
            this.failCount = failCount;
            this.initCount = initCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getFailCount() {
            return failCount;
        }

        public void setFailCount(int failCount) {
            this.failCount = failCount;
        }

        public int getInitCount() {
            return initCount;
        }

        public void setInitCount(int initCount) {
            this.initCount = initCount;
        }
    }

    public static <T extends ITccBranchRequest> DefaultTccProcessorContext<T> create(TccBranchContext<T> tccBranchContext, TccProcessorRequestDto tccProcessorRequestDto){
        DefaultTccProcessorContext context = new DefaultTccProcessorContext();
        context.setTccBranchContext(tccBranchContext);
        context.setTccProcessorRequestDto(tccProcessorRequestDto);
        context.setTryBranchResult(DefaultTccProcessorContext.buildBranchResult(Collections.synchronizedMap(FrameUtils.newLinkedHashMap()), ResultStatus.INIT, DefaultTccProcessorContext.buildBranchStatusCounter(0, 0, 0)));
        context.setConfirmBranchResult(DefaultTccProcessorContext.buildBranchResult(Collections.synchronizedMap(FrameUtils.newLinkedHashMap()), ResultStatus.INIT, DefaultTccProcessorContext.buildBranchStatusCounter(0, 0, 0)));
        context.setCancelBranchResult(DefaultTccProcessorContext.buildBranchResult(Collections.synchronizedMap(FrameUtils.newLinkedHashMap()), ResultStatus.INIT, DefaultTccProcessorContext.buildBranchStatusCounter(0, 0, 0)));
        return context;
    }

    public static <T extends ITccBranchRequest> BranchResult<T> buildBranchResult(Map<ITccBranch<T>, CurTccBranchResponse> branchResponseMap, ResultStatus resultStatus, BranchStatusCounter branchStatusCounter) {
        return new BranchResult(branchResponseMap, resultStatus, branchStatusCounter);
    }

    public static BranchStatusCounter buildBranchStatusCounter(int successCount, int failCount, int initCount) {
        return new BranchStatusCounter(successCount, failCount, initCount);
    }
}
