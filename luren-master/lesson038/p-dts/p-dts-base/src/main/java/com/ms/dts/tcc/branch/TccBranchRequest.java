package com.ms.dts.tcc.branch;

/**
 * <b>description</b>：tcc事务处理参数 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TccBranchRequest implements ITccBranchRequest {
    private String tid;
    private Integer busType;
    private String busId;

    @Override
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Integer getBusType() {
        return busType;
    }

    public void setOrderType(Integer busType) {
        this.busType = busType;
    }

    @Override
    public String getBusId() {
        return busId;
    }

    public void setOrderId(String busId) {
        this.busId = busId;
    }
}
