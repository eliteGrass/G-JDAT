package com.itsoku.common;

import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDto<T> {
    /**
     * 响应代码
     */
    private String state;
    /**
     * 子代码
     */
    private String subCode;
    /**
     * 信息提示
     */
    private String msg;
    /**
     * 成功之后返回的数据
     */
    private T data;
    /**
     * 扩展数据
     */
    private Map extData;

    /**
     * 时间
     */
    private Long time;

    /**
     * 添加扩展数据
     *
     * @param key
     * @param value
     * @return
     */
    public ResultDto<T> putExtData(Object key, Object value) {
        if (this.extData == null) {
            this.extData = new LinkedHashMap<>();
        }
        this.extData.put(key, value);
        return this;
    }

    /**
     * 添加扩展数据
     *
     * @param map
     * @return
     */
    public ResultDto<T> putExtDatas(Map<Object, Object> map) {
        if (this.extData == null) {
            this.extData = new LinkedHashMap<>();
        }
        this.extData.putAll(map);
        return this;
    }

    /**
     * 添加扩展数据
     *
     * @param key
     * @param value
     * @return
     */
    public ResultDto<T> removeExtData(Object key, Object value) {
        if (this.extData != null) {
            this.extData.remove(key);
        }
        return this;
    }

    public Map<String, Object> map(){
        return FrameUtils.javaBeanToMap(this);
    }

    /**
     * 获取结果，获取之前对结果进行校验
     *
     * @return
     */
    public ResultDto<T> isOK() {
        return ResultUtils.isOK(this);
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean success() {
        return ResultUtils.isSuccess(this);
    }

    /**
     * 是否失败
     *
     * @return
     */
    public boolean error() {
        return ResultUtils.isError(this);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubCode() {
        if (subCode == null)
            return "";
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

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map getExtData() {
        return extData;
    }

    public void setExtData(Map extData) {
        this.extData = extData;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * 获取data，获取之前对结果进行校验
     *
     * @return
     */
    @Transient
    public T getSuccessData() {
        if (ResultUtils.isSuccess(this)) {
            return this.data;
        }

        // 失败时 data没有数据
        throw new ResultException(this);
    }
}
