package com.itsoku.common;

import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2018-08-02 10:35 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public class BusinessException extends RuntimeException {
    /**
     * 错误代码
     */
    private String code;
    /**
     * 子代码
     */
    private String subCode;
    /**
     * 信息提示
     */
    private String msg;
    /**
     * 扩展数据
     */
    private Map extData;

    public BusinessException() {
    }

    public BusinessException(String code, String subCode, String msg, Map extData) {
        super(msg);
        this.code = code;
        this.subCode = subCode;
        this.msg = msg;
        this.extData = extData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getExtData() {
        return extData;
    }

    public void setExtData(Map extData) {
        this.extData = extData;
    }
}
