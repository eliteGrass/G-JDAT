package com.itsoku.lesson025.page;

/**
 * <b>description</b>：分页查询接口 <br>
 * <b>time</b>： 21:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IPageQuery {
    /**
     * 页码
     *
     * @return
     */
    int getPageNum();

    /**
     * 每页大小
     *
     * @return
     */
    int getPageSize();

    /**
     * 是否需要分页
     *
     * @return
     */
    boolean count();
}
