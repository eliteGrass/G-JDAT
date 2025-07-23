package com.itsoku.lesson007.dto;

import com.itsoku.lesson007.excel.ExcelExportRequest;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class UserExportRequest extends ExcelExportRequest {
    /**
     * 要导出的用户id列表，不传，则导出所有用户记录
     */
    private List<Integer> userIdList;

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }
}
