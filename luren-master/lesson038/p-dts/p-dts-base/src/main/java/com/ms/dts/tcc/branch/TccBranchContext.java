package com.ms.dts.tcc.branch;

import com.itsoku.utils.FrameUtils;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.base.model.TccRecordPO;

import java.io.Serializable;
import java.util.Map;

/**
 * <b>description</b>：分支上下文 <br>
 * <b>time</b>：2024/4/29 9:22 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TccBranchContext<T extends ITccBranchRequest> implements Serializable {
    //请求封装，分布式事务分支共用
    private T request;
    //当前分支结果
    private CurTccBranchResponse response;
    //业务日志
    protected TccBusBranchLogPO tccBusBranchLogPO;
    //异常信息
    private ExceptionDto exceptionDto;
    //扩展数据
    private Map data = FrameUtils.newHashMap();
    //tcc订单
    private TccRecordPO tccRecordPO;

    public TccBranchContext() {
    }

    public TccBranchContext(T request, CurTccBranchResponse response) {
        this.request = request;
        this.response = response;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public CurTccBranchResponse getResponse() {
        return response;
    }

    public void setResponse(CurTccBranchResponse response) {
        this.response = response;
    }

    public TccBusBranchLogPO getTccBusBranchLogModel() {
        return tccBusBranchLogPO;
    }

    public void setTccBusBranchLogModel(TccBusBranchLogPO tccBusBranchLogPO) {
        this.tccBusBranchLogPO = tccBusBranchLogPO;
    }

    public ExceptionDto getExceptionDto() {
        return exceptionDto;
    }

    public void setExceptionDto(ExceptionDto exceptionDto) {
        this.exceptionDto = exceptionDto;
    }

    public TccRecordPO getTccRecordModel() {
        return tccRecordPO;
    }

    public void setTccRecordModel(TccRecordPO tccRecordPO) {
        this.tccRecordPO = tccRecordPO;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
