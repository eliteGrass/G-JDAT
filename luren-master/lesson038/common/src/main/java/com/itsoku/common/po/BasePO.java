package com.itsoku.common.po;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.itsoku.utils.FrameUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/17 11:48 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class BasePO implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private Map<Object, Object> extData;

    public Map<Object, Object> getExtData() {
        if(extData==null){
            extData = new HashMap<>();
        }
        return extData;
    }

    public void setExtData(Map<Object, Object> extData) {
        this.extData = extData;
    }

    public void putExtData(Object key, Object value) {
        if (extData == null) {
            extData = FrameUtils.newLinkedHashMap();
        }
        extData.put(key, value);
    }
}
