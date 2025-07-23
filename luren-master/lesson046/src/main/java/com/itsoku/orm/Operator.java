package com.itsoku.orm;

/**
 * 关系操作符号
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum Operator {
    beginWith("like"),
    notBeginWith("not like"),
    contains("like"),
    notContains("not like"),
    endWith("like"),
    notEndWith("not like"),
    between("between"),
    notBetween("not between"),
    blank(" = ''"),
    notBlank(" != ''"),
    equal("="),
    notEqual("!="),
    greaterThan(">"),
    greaterEqual(">="),
    lessEqual("<="),
    lessThan("<"),
    isNull("is null"),
    isNotNull("is not null"),
    in("in"),
    notIn("not in"),
    custom("");

    private final String value;

    Operator(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
