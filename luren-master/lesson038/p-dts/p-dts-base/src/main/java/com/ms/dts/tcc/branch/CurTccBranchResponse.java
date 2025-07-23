package com.ms.dts.tcc.branch;

import com.ms.dts.base.ResultStatus;

import java.io.Serializable;

/**
 * <b>description</b>：当前tcc分支事务步骤处理结果 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class CurTccBranchResponse implements Serializable {
    //tid
    protected String tid;
    //状态
    protected ResultStatus resultStatus = ResultStatus.INIT;
    //消息
    protected String msg;


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
