package com.itsoku.lesson007.excel;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ExcelExportField {
    /**
     * 字段的名称
     */
    private String fieldName;

    /**
     * 字段描述
     */
    private String fieldDesc;

    public ExcelExportField() {
    }

    public ExcelExportField(String fieldName, String fieldDesc) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }
}
