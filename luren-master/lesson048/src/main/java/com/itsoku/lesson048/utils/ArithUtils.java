package com.itsoku.lesson048.utils;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 23:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ArithUtils {
    public final static BigDecimal ZERO = new BigDecimal("0.00");

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

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    /**
     * v1-v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * v1<=0?
     *
     * @param v1
     * @return
     */
    public static boolean le0(BigDecimal v1) {
        return le(v1, ZERO);
    }

    /**
     * v1<0?
     *
     * @param v1
     * @return
     */
    public static boolean lt0(BigDecimal v1) {
        return lt(v1, ZERO);
    }


}
