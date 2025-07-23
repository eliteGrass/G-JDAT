package com.itsoku.lesson025.page;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 21:10 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class PageQuery implements IPageQuery {
    /**
     * 页码，从1开始
     */
    private int pageNum = 1;
    /**
     * 每页大小
     */
    private int pageSize = 10;
    /**
     * 是否需要统计总行数
     */
    private boolean count = true;

    /**
     * 获取分页请求参数
     *
     * @param pageNum  页码，从1开始
     * @param pageSize 每页大小
     * @param count    是否需要统计总行数
     * @return
     */
    public static PageQuery of(int pageNum, int pageSize, boolean count) {
        return new PageQuery(pageNum, pageSize, count);
    }

    public PageQuery() {
    }

    public PageQuery(int pageNum, int pageSize, boolean count) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    @Override
    public boolean count() {
        return this.count;
    }

}
