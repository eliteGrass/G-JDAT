package com.itsoku.lesson007.excel;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ExcelExportResponse {
    //导出的excel文件名称
    private String excelName;
    // sheet列表数据
    private List<ExcelSheet> sheetList;

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public List<ExcelSheet> getSheetList() {
        return sheetList;
    }

    public void setSheetList(List<ExcelSheet> sheetList) {
        this.sheetList = sheetList;
    }
}
