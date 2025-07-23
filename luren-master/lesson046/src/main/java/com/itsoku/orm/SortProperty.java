package com.itsoku.orm;

import java.io.Serializable;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SortProperty implements Serializable {
    private String propertyName;
    private Sort sort = Sort.ASC;

    public SortProperty() {
    }

    public SortProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public SortProperty(String propertyName, Sort sort) {
        this.propertyName = propertyName;
        this.sort = sort;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
