package com.itsoku.lesson043.utils;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 23:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ArithUtils {
    /**
     * v1>v2?
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean gt(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) > 0;
    }

    /**
     * v1>=v2?
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean ge(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) >= 0;
    }

    /**
     * v1<v2?
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean lt(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) < 0;
    }


    /**
     * v1<=v2?
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean le(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) <= 0;
    }

}
