package com.itsoku.lesson093;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/10 23:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class BigDecimalUtils {

    /**
     * 除法默认保留精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 默认舍入模式（ROUND_HALF_UP：四舍五入）
     */
    private static final int DEF_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private static final String FORMAT_1 = "#.##";

    /**
     * 将字符串转换为 BigDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal of(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    /**
     * v1 + v2
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    /**
     * args[0] + args[1] + ... + args[n]
     *
     * @param args
     * @return
     */
    public static BigDecimal add(BigDecimal... args) {
        if (args == null || args.length <= 1) {
            throw new IllegalArgumentException("args Contains at least two parameters");
        }
        BigDecimal result = args[0];
        for (int i = 1; i < args.length; i++) {
            result = add(result, args[i]);
        }
        return result;
    }


    /**
     * v1 - v2
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * args[0] - args[1] - ... - args[n]
     *
     * @param args
     * @return
     */
    public static BigDecimal subtract(BigDecimal... args) {
        if (args == null || args.length <= 1) {
            throw new IllegalArgumentException("args Contains at least two parameters");
        }
        BigDecimal result = args[0];
        for (int i = 1; i < args.length; i++) {
            result = subtract(result, args[i]);
        }
        return result;
    }

    /**
     * v1 * v2
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * args[0] * args[1] * ... * args[n]
     *
     * @return 两个参数的积
     */

    public static BigDecimal multiply(BigDecimal... args) {
        if (args == null || args.length <= 1) {
            throw new IllegalArgumentException("args Contains at least two parameters");
        }
        BigDecimal result = args[0];
        for (int i = 1; i < args.length; i++) {
            result = multiply(result, args[i]);
        }
        return result;
    }


    /**
     * v1 / v2
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, DEF_DIV_SCALE, DEF_ROUNDING_MODE);
    }

    /**
     * args[0] / args[1] / ... / args[n]
     *
     * @return 两个参数的积
     */

    public static BigDecimal divide(BigDecimal... args) {
        if (args == null || args.length <= 1) {
            throw new IllegalArgumentException("args Contains at least two parameters");
        }
        BigDecimal result = args[0];
        for (int i = 1; i < args.length; i++) {
            result = divide(result, args[i]);
        }
        return result;
    }

    /**
     * 四舍五入，保留两位小数
     *
     * @param d
     * @return
     */
    public static BigDecimal round(BigDecimal d) {
        if (d == null) {
            return null;
        }
        return d.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


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
     * v1 == v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean eq(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) == 0;
    }

    /**
     * 判断 values 是否等于 0？
     *
     * @param value
     * @return
     */
    public static boolean eq0(BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) == 0;
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
     * 格式化，保留2为小数
     *
     * @param value
     * @return
     */
    public static String format(BigDecimal value) {
        if (value == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat(FORMAT_1);
        return df.format(value);
    }

    /**
     * 格式化
     *
     * @param value
     * @param pattern 模式字符串，如：#.##
     * @return
     */
    public static String format(BigDecimal value, String pattern) {
        if (value == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

}
