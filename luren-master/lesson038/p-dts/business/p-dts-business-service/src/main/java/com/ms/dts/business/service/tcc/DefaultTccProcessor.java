package com.ms.dts.business.service.tcc;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.common.ResultDto;
import com.itsoku.enums.TccBusTypeEnum;
import com.itsoku.utils.CollUtils;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.api.base.TccRecordClient;
import com.ms.dts.api.tcc.TccBusClient;
import com.ms.dts.base.ITccProcessor;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.tcc.branch.*;
import com.ms.dts.tcc.dto.TccProcessorRequestDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <b>description</b>：默认tcc处理器 <br>
 * <b>time</b>：2024/4/25 9:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public abstract class DefaultTccProcessor<T extends ITccBranchRequest> implements ITccProcessor<T>, InitializingBean, BeanNameAware {

    //当前service的应用名称
    @Value("${service.url}")
    protected String serviceApplicationName;
    //当前bean的名称
    protected String beanname;
    protected List<ITccBranch<T>> tccList;
    @Autowired
    protected TccRecordClient tccRecordClient;
    @Autowired
    protected TccBusClient tccBusClient;

    /**
     * 获取订单类型
     *
     * @return
     */
    protected abstract TccBusTypeEnum getBusType();

    /**
     * 将 {@link TccRecordPO#getRequestData()} 转换为对象
     *
     * @return <p>
     */
    public abstract TypeReference<TccBranchContext<T>> getTccRecordRequestDataTypeReference();

    /**
     * 请求前置处理
     *
     * @param tccBranchContext
     * @return
     */
    protected TccBranchContext<T> disposeBefore(TccBranchContext<T> tccBranchContext) {
        return tccBranchContext;
    }

    /**
     * 获取订单id
     *
     * @return
     */
    protected String defaultOrderId() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 执行完毕之后是否同步上传结果
     *
     * @return
     * @see TccProcessorRequestDto#isSync()
     */
    protected boolean isSyncUploadResult(T request) {
        return false;
    }

    /**
     * try执行完毕是否继续同步执行后面的步骤
     *
     * @return
     * @see TccProcessorRequestDto#isSync()
     */
    protected boolean isSync(T request) {
        return true;
    }

    @Override
    public ResultDto<TccBranchContext<T>> dispose(T request) {
        DefaultTccProcessorContext<T> tccProcessorContext;
        TccBranchContext<T> tccBranchContext = new TccBranchContext(request, null);
        tccBranchContext = this.disposeBefore(tccBranchContext);
        Integer busType = request.getBusType();
        if (Objects.isNull(busType)) {
            busType = this.getBusType().getCode();
        }
        String busId = request.getBusId();
        if (Objects.isNull(busId)) {
            busId = this.defaultOrderId();
        }
        if (Objects.isNull(busType)) {
            FrameUtils.throwBaseException("busType不能为空!");
        } else if (Objects.isNull(busId)) {
            FrameUtils.throwBaseException("busId不能为空!");
        }
        request.setOrderType(busType);
        request.setOrderId(busId);
        // 根据 busType + busId 获取分布式事务记录
        TccRecordPO tccRecordPO = this.tccRecordClient.getModelByOrder(busType, busId).getSuccessData();
        if (Objects.isNull(tccRecordPO)) {
            // 不存在则创建
            Long curtime = FrameUtils.getTime();
            tccRecordPO = this.tccRecordClient.insert(TccRecordPO.builder().
                            busType(busType).
                            busId(busId).
                            classname(this.getClass().getName()).
                            serviceApplicationName(this.serviceApplicationName).
                            beanname(this.beanname).
                            requestData(FrameUtils.json(tccBranchContext)).
                            nextDisposeTime(FrameUtils.getNextDisposeTime(0, curtime)).
                            addtime(curtime).
                            version(0L).build()).
                    getSuccessData();
        }
        // 调用分支的方法（try、confirm或cancel）
        tccProcessorContext = this.disposeIn(TccProcessorRequestDto.builder().
                tccRecordPO(tccRecordPO).
                syncUploadResult(this.isSyncUploadResult(request)).
                sync(this.isSync(request)).
                build());
        this.errorAfterDispose(tccProcessorContext);
        ResultDto<TccBranchContext<T>> currentStepResultDto = tccProcessorContext.getCurrentStepResultDto();
        TccBranchContext<T> context = currentStepResultDto.getData();
        if (Objects.nonNull(context)) {
            context.setTccRecordModel(this.tccRecordClient.getModelById(tccRecordPO.getId()).getSuccessData());
        }
        return currentStepResultDto;

    }

    /**
     * 异常处理
     *
     * @param tccProcessorContext
     */
    protected void errorAfterDispose(DefaultTccProcessorContext<T> tccProcessorContext) {
        if (CollUtils.isNotEmpty(tccProcessorContext.getExceptionDtoList())) {
            ExceptionDto exceptionDto = tccProcessorContext.getExceptionDtoList().get(0);
            FrameUtils.throwBaseException(exceptionDto.getCode(), exceptionDto.getSubCode(), exceptionDto.getMsg(), exceptionDto.getExtData());
        }
    }


    protected ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

    /**
     * 处理之后
     *
     * @param tccProcessorContext
     */
    public void callAfter(DefaultTccProcessorContext<T> tccProcessorContext) {
        if (tccProcessorContext == null) {
            return;
        }
        if (tccProcessorContext.getTccProcessorRequestDto().isSyncUploadResult()) {
            this.uploadResult(tccProcessorContext.getTccProcessorResponseDto());
        } else {
            this.executorService.submit(() -> uploadResult(tccProcessorContext.getTccProcessorResponseDto()));
        }
    }

    /**
     * 上传结果
     *
     * @param tccProcessorResponseDto
     */
    private void uploadResult(TccProcessorResponseDto tccProcessorResponseDto) {
        try {
            if (Objects.isNull(tccProcessorResponseDto)) {
                return;
            }
            ResultDto<Void> resultDto = tccBusClient.uploadTccResult(tccProcessorResponseDto);
            log.info("asyncUploadTccProcessResponseDto:{}", resultDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ResultDto<TccProcessorResponseDto> recover(TccProcessorRequestDto request) {
        return ResultUtils.successData(this.disposeIn(request).getTccProcessorResponseDto());
    }

    protected DefaultTccProcessorContext<T> disposeIn(TccProcessorRequestDto request) {
        final DefaultTccProcessorContext<T> context = DefaultTccProcessorContext.create(this.buildTccBranchContext(request), request);
        try {
            //执行tcc->try
            this.callTry(context);
            if (request.isSync()) {
                this.disposeInStep2(context);
            } else {
                this.executorService.submit(() -> {
                    try {
                        disposeInStep2(context);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
            }
            //处理结果
            this.disposeResult(context);
            context.setTccProcessorResponseDto(this.buildTccProcessorResponse(context));
            return context;
        } finally {
            this.callAfter(context);
        }
    }

    protected void callFinallyMethod(DefaultTccProcessorContext<T> context) {
        context.setFinallyOver(this.finallyMethod(context));
    }

    private void disposeInStep2(DefaultTccProcessorContext<T> context) {
        if (context.getTryBranchResult().getResultStatus() == ResultStatus.SUCCESS) {
            //执行tcc->confirm
            this.callConfirm(context);
        } else if (context.getTryBranchResult().getResultStatus() == ResultStatus.FAIL) {
            //执行tcc->cancel
            this.callCancel(context);
        }
    }

    /**
     * 结果处理
     *
     * @param context
     * @throws Exception
     */
    protected void disposeResult(DefaultTccProcessorContext<T> context) {
        //事务最终结果
        ResultStatus resultStatus = ResultStatus.INIT;
        //事务是否已结束
        Boolean isFinish = Boolean.FALSE;
        if (context.getTryBranchResult().getResultStatus() == ResultStatus.SUCCESS) {
            if (context.getConfirmBranchResult().getResultStatus() == ResultStatus.SUCCESS) {
                resultStatus = ResultStatus.SUCCESS;
                isFinish = Boolean.TRUE;
            }
        } else if (context.getTryBranchResult().getResultStatus() == ResultStatus.FAIL) {
            if (context.getCancelBranchResult().getResultStatus() == ResultStatus.SUCCESS) {
                resultStatus = ResultStatus.FAIL;
                isFinish = Boolean.TRUE;
            }
        }
        //tcc执行完毕之后，调用finally方法
        if (isFinish) {
            this.callFinallyMethod(context);
        }
        //finnaly结束之后，tcc才算最终结束
        if (context.getFinallyOver()) {
            context.setResultStatus(resultStatus);
            context.setFinish(isFinish);
        }
    }

    /**
     * 构建处理相应结果
     *
     * @param context
     * @return
     * @throws Exception
     */
    protected TccProcessorResponseDto buildTccProcessorResponse(DefaultTccProcessorContext<T> context) {
        List<TccBranchLogPO> tccBranchLogPOList = FrameUtils.newArrayList();
        tccBranchLogPOList.addAll(context.getTryBranchResult().getBranchResponseMap().values().stream().map(item -> buildTccBranchLogModel(item.getSuccessData().getTccBusBranchLogModel())).collect(Collectors.toList()));
        tccBranchLogPOList.addAll(context.getConfirmBranchResult().getBranchResponseMap().values().stream().map(item -> buildTccBranchLogModel(item.getSuccessData().getTccBusBranchLogModel())).collect(Collectors.toList()));
        tccBranchLogPOList.addAll(context.getCancelBranchResult().getBranchResponseMap().values().stream().map(item -> buildTccBranchLogModel(item.getSuccessData().getTccBusBranchLogModel())).collect(Collectors.toList()));
        TccProcessorResponseDto tccProcessorResponseDto = new TccProcessorResponseDto();
        tccProcessorResponseDto.setTccRecordId(context.getTccProcessorRequestDto().getTccRecordPO().getId());
        tccProcessorResponseDto.setResultStatus(context.getResultStatus());
        tccProcessorResponseDto.setTccBranchLogPOList(tccBranchLogPOList);
        tccProcessorResponseDto.setFinish(context.getFinish());
        return tccProcessorResponseDto;
    }

    private TccBranchLogPO buildTccBranchLogModel(TccBusBranchLogPO tccBusBranchLogPO) {
        TccBranchLogPO tccBranchLogPO = new TccBranchLogPO();
        BeanUtils.copyProperties(tccBusBranchLogPO, tccBranchLogPO);
        return tccBranchLogPO;
    }


    /**
     * 构建请求参数
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected TccBranchContext<T> buildTccBranchContext(TccProcessorRequestDto request) {
        TccBranchContext<T> tccBranchContext = JSONUtil.toBean(request.getTccRecordPO().getRequestData(), this.getTccRecordRequestDataTypeReference(), true);
        tccBranchContext.getRequest().setTid(request.getTccRecordPO().getId());
        return tccBranchContext;
    }

    /**
     * 调用
     *
     * @param tccProcessorContext
     * @param tccBranch
     * @param methodEnums
     * @return
     * @throws Exception
     */
    private ResultDto<TccBranchContext<T>> invokeTccBranchMethod(DefaultTccProcessorContext tccProcessorContext, ITccBranch<T> tccBranch, TccBranchMethodEnums methodEnums) {
        try {
            ResultDto<TccBranchContext<T>> tccBranchContextResultDto = null;
            if (methodEnums == TccBranchMethodEnums.try1) {
                tccBranchContextResultDto = tccBranch.try1(tccProcessorContext.getTccBranchContext());
            } else if (methodEnums == TccBranchMethodEnums.confirm) {
                tccBranchContextResultDto = tccBranch.confirm(tccProcessorContext.getTccBranchContext());
            } else if (methodEnums == TccBranchMethodEnums.cancel) {
                tccBranchContextResultDto = tccBranch.cancel(tccProcessorContext.getTccBranchContext());
            }

            // 处理没有成功
            if (tccBranchContextResultDto != null && !tccBranchContextResultDto.success()) {
                log.error("分支{}:{}处理失败，原因：{}", tccBranch.getClass().getName(), methodEnums.getMethodName(), tccBranchContextResultDto.getMsg());
            }

            tccProcessorContext.setCurrentStepResultDto(tccBranchContextResultDto);
            ExceptionDto exceptionDto = tccBranchContextResultDto.getSuccessData().getExceptionDto();
            if (Objects.nonNull(exceptionDto)) {
                if (Objects.isNull(tccProcessorContext.getExceptionDtoList())) {
                    tccProcessorContext.setExceptionDtoList(FrameUtils.newArrayList());
                }
                tccProcessorContext.getExceptionDtoList().add(exceptionDto);
            }
            return tccBranchContextResultDto;
        } catch (Exception ex) {
            log.error("分支{}:{}处理失败，原因：{}", tccBranch.getClass().getName(), methodEnums.getMethodName(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * tcc->try阶段
     *
     * @param tccProcessorContext
     * @throws Exception
     */
    protected void callTry(DefaultTccProcessorContext tccProcessorContext) {
        DefaultTccProcessorContext.BranchResult tryBranchResult = tccProcessorContext.getTryBranchResult();
        Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap = tryBranchResult.getBranchResponseMap();
        for (ITccBranch<T> branch : this.tccList) {
            ResultDto<TccBranchContext<T>> tccBranchContextResultDto = this.invokeTccBranchMethod(tccProcessorContext, branch, TccBranchMethodEnums.try1);
            tccProcessorContext.setTccBranchContext(tccBranchContextResultDto.getSuccessData());
            CurTccBranchResponse response = tccProcessorContext.getTccBranchContext().getResponse();
            ResultStatus resultStatus = response.getResultStatus();
            putBranchResult(branchResponseMap, branch, tccBranchContextResultDto);
            // 循环调try，成功才继续向下，否则退出
            if (ResultStatus.SUCCESS != resultStatus) {
                break;
            }
        }
        tryBranchResult.setBranchStatusCounter(this.buildBranchStatusCounter(branchResponseMap));
        DefaultTccProcessorContext.BranchStatusCounter branchStatusCounter = tryBranchResult.getBranchStatusCounter();
        if (branchStatusCounter.getSuccessCount() == tccList.size()) {
            tryBranchResult.setResultStatus(ResultStatus.SUCCESS);
        } else if (branchStatusCounter.getFailCount() >= 1) {
            tryBranchResult.setResultStatus(ResultStatus.FAIL);
        } else {
            tryBranchResult.setResultStatus(ResultStatus.INIT);
        }
    }

    /**
     * 将结果放入分支步骤中
     *
     * @param branchResponseMap
     * @param branch
     * @param tccBranchContextResultDto
     */
    private void putBranchResult(Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap, ITccBranch<T> branch, ResultDto<TccBranchContext<T>> tccBranchContextResultDto) {
        ResultDto<TccBranchContext<T>> resultDto = new ResultDto<>();
        BeanUtils.copyProperties(tccBranchContextResultDto, resultDto);
        TccBranchContext<T> tccBranchContext = tccBranchContextResultDto.getSuccessData();
        TccBranchContext branchContext = new TccBranchContext();
        BeanUtils.copyProperties(tccBranchContext, branchContext);
        resultDto.setData(branchContext);
        branchResponseMap.put(branch, resultDto);
    }

    private DefaultTccProcessorContext.BranchStatusCounter buildBranchStatusCounter(Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> map) {
        int successCount = 0;
        int failCount = 0;
        int initCount = 0;
        for (Map.Entry<ITccBranch<T>, ResultDto<TccBranchContext<T>>> entry : map.entrySet()) {
            ResultStatus resultStatus = entry.getValue().getSuccessData().getResponse().getResultStatus();
            if (ResultStatus.SUCCESS == resultStatus) {
                successCount++;
            } else if (ResultStatus.FAIL == resultStatus) {
                failCount++;
            } else if (ResultStatus.INIT == resultStatus) {
                initCount++;
            }
        }
        return DefaultTccProcessorContext.buildBranchStatusCounter(successCount, failCount, initCount);
    }

    /**
     * tcc->confirm阶段
     *
     * @param tccProcessorContext
     * @throws Exception
     */
    protected void callConfirm(DefaultTccProcessorContext tccProcessorContext) {
        DefaultTccProcessorContext.BranchResult<T> branchResult = tccProcessorContext.getConfirmBranchResult();
        this.call(tccProcessorContext, branchResult, branch -> {
            try {
                return this.invokeTccBranchMethod(tccProcessorContext, branch, TccBranchMethodEnums.confirm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, TccBranchMethodEnums.confirm);
    }

    /**
     * tcc->cancel阶段
     *
     * @param tccProcessorContext
     * @throws Exception
     */
    protected void callCancel(DefaultTccProcessorContext tccProcessorContext) {
        DefaultTccProcessorContext.BranchResult<T> branchResult = tccProcessorContext.getCancelBranchResult();
        this.call(tccProcessorContext, branchResult, branch -> {
            try {
                return this.invokeTccBranchMethod(tccProcessorContext, branch, TccBranchMethodEnums.cancel);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, TccBranchMethodEnums.cancel);
    }

    private void call(DefaultTccProcessorContext<T> context,
                      DefaultTccProcessorContext.BranchResult<T> branchResult,
                      Function<ITccBranch<T>, ResultDto<TccBranchContext<T>>> callFun,
                      TccBranchMethodEnums tccBranchMethodEnums) {
        Map<ITccBranch<T>, ResultDto<TccBranchContext<T>>> branchResponseMap = branchResult.getBranchResponseMap();
        List<Map.Entry<ITccBranch<T>, ResultDto<TccBranchContext<T>>>> tryBranch = context.getTryBranchResult().getBranchResponseMap().entrySet().stream().collect(Collectors.toList());
        //如果执行cancel，则分支的顺序和try相反
        if (tccBranchMethodEnums == TccBranchMethodEnums.cancel) {
            Collections.reverse(tryBranch);
        }
        for (Map.Entry<ITccBranch<T>, ResultDto<TccBranchContext<T>>> entry : tryBranch) {
            //对于try成功的，做confirm或者cancel处理
            if (entry.getValue().getSuccessData().getResponse().getResultStatus() == ResultStatus.SUCCESS) {
                ITccBranch<T> branch = entry.getKey();
                ResultDto<TccBranchContext<T>> trTccBranchContext = callFun.apply(branch).isOK();
                context.setTccBranchContext(trTccBranchContext.getSuccessData());
                CurTccBranchResponse response = trTccBranchContext.getSuccessData().getResponse();
                ResultStatus resultStatus = response.getResultStatus();
                this.putBranchResult(branchResponseMap, branch, trTccBranchContext);
                if (ResultStatus.SUCCESS != resultStatus) {
                    break;
                }
            }
        }
        branchResult.setBranchStatusCounter(this.buildBranchStatusCounter(branchResponseMap));
        DefaultTccProcessorContext.BranchStatusCounter branchStatusCounter = branchResult.getBranchStatusCounter();
        if (branchStatusCounter.getSuccessCount() == context.getTryBranchResult().getBranchStatusCounter().getSuccessCount()) {
            branchResult.setResultStatus(ResultStatus.SUCCESS);
        } else if (branchStatusCounter.getFailCount() >= 1) {
            branchResult.setResultStatus(ResultStatus.FAIL);
        } else {
            branchResult.setResultStatus(ResultStatus.INIT);
        }
    }

    @Override
    public void afterPropertiesSet() {
        tccList = FrameUtils.newArrayList();
        Map<Integer, ITccBranch<T>> map = FrameUtils.newLinkedHashMap();
        DefaultTccProcessor $this = this;
        ReflectionUtils.doWithFields($this.getClass(), field -> {
            if (ITccBranch.class.isAssignableFrom(field.getType())) {
                if (!field.isAccessible()) {
                    ReflectionUtils.makeAccessible(field);
                }
                ITccBranch branch = (ITccBranch) ReflectionUtils.getField(field, this);
                int value = field.getAnnotation(TccBranchOrder.class).value();
                if (map.containsKey(value)) {
                    FrameUtils.throwBaseException($this.getClass() + " 中 @TccBranchOrder 存在相等的值[value = " + value + "],分支的执行顺序不能相同！");
                }
                map.put(value, branch);
            }
        });
        map.keySet().stream().sorted(Integer::compareTo).map(map::get).forEach(tccList::add);
    }

    @Override
    public void setBeanName(String name) {
        this.beanname = name;
    }

    /**
     * finally 方法，tcc中所有业务逻辑执行完毕之后会调用该方法
     * 如果返回true，表示finally执行成功
     * 如果返回false，tcc服务会在重试补偿中继续调用finally方法
     * 此方法内部需确保幂等性操作
     *
     * @param context
     * @return
     * @throws Exception
     */
    protected boolean finallyMethod(DefaultTccProcessorContext<T> context) {
        return true;
    }
}
