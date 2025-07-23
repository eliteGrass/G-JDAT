package com.itsoku.lesson093;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/10 23:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class BigDecimalTest {

    @Test
    public void m1() {
        double a = 4.015, b = 100.00;
        System.out.println(a * b);

        double c = 123.30, d = 100.00;
        System.out.println(c / d);
    }

    @Test
    public void m2() {
        BigDecimal a = new BigDecimal("4.015");
        BigDecimal b = new BigDecimal("100.00");
        System.out.println(a.multiply(b));

        BigDecimal c = new BigDecimal("123.30");
        BigDecimal d = new BigDecimal("100.00");
        System.out.println(c.divide(d));
    }

    @Test
    public void m3() {
        System.out.println(new BigDecimal("0.1").compareTo(new BigDecimal("0.2")));
        System.out.println(new BigDecimal("0.2").compareTo(new BigDecimal("0.1")));
        System.out.println(new BigDecimal("0.123").compareTo(new BigDecimal("0.123000000")));
    }

    @Test
    public void m4() {
        System.out.println(new BigDecimal("0.12345678").setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println(new BigDecimal("0.45678910").setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void m5() {
        //1、通过字符串构造 BigDecimal
        System.out.println(BigDecimalUtils.of("0.123"));

        //2、加法
        System.out.println(BigDecimalUtils.add(new BigDecimal("0.111"), new BigDecimal("0.123")));

        //3、减法
        System.out.println(BigDecimalUtils.subtract(new BigDecimal("0.111"), new BigDecimal("0.123")));

        //4、乘法
        System.out.println(BigDecimalUtils.multiply(new BigDecimal("0.111"), new BigDecimal("0.123")));

        //5、除法
        System.out.println(BigDecimalUtils.divide(new BigDecimal("0.111"), new BigDecimal("0.123")));

        //6、四舍五入，结果保留2位小数，金融系统中通常会将计算的结果保留到分，此时就可以用这个方法
        System.out.println(BigDecimalUtils.round(BigDecimalUtils.divide(new BigDecimal("0.111"), new BigDecimal("0.123"))));

        //7、判断 v1<v2?
        BigDecimal v1 = new BigDecimal("0.111");
        BigDecimal v2 = new BigDecimal("0.123");
        System.out.println(BigDecimalUtils.lt(v1, v2));

        //8、判断2个数是否相等 0.111 == 0.11100000000，相等
        System.out.println(BigDecimalUtils.eq(v1, new BigDecimal("0.11100000000"))); // true

        //9、判断是否等于0
        System.out.println(BigDecimalUtils.eq0(new BigDecimal("0")));  // true
        System.out.println(BigDecimalUtils.eq0(new BigDecimal("0.000"))); // ture
        System.out.println(BigDecimalUtils.eq0(new BigDecimal("0.001"))); // false

        //10、格式化
        System.out.println(BigDecimalUtils.format(BigDecimalUtils.of("123.45678"))); // 123.46
    }
}
