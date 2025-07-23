package com.itsoku.lesson007.excel;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ExcelExportRequest {
    /**
     * excel名称
     */
    private String excelName;

    /**
     * sheet的名称
     */
    private String sheetName;

    /**
     * 导出字段有序列表
     */
    private List<ExcelExportField> fieldList;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public List<ExcelExportField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ExcelExportField> fieldList) {
        this.fieldList = fieldList;
    }
}
