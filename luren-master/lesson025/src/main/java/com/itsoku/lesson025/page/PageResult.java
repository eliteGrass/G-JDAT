package com.itsoku.lesson025.page;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 20:52 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class PageResult<T> {
    /**
     * 页码
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 记录总数
     */
    private long total;

    private List<T> list;

    public static <T> PageResult<T> of(List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.list = (List<T>) list;
        if (list instanceof Page) {
            Page<?> pg = (Page<?>) list;
            result.total = pg.getTotal();
            result.setPageNum(pg.getPageNum());
            result.setPageSize(pg.getPageSize());
        } else {
            result.total = list.size();
        }
        return result;
    }
}
