package com.itsoku.lesson043.idempotent;


import java.util.Objects;

/**
 * 幂等调用状态
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum IdempotentCallStatusEnum {
    init(0, "处理中"),
    success(1, "处理成功"),
    fail(-1, "处理失败");
    //状态值
    private final Integer value;
    //状态说明
    private final String desc;

    IdempotentCallStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static IdempotentCallStatusEnum getByValue(Integer value) {
        for (IdempotentCallStatusEnum item : IdempotentCallStatusEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        IdempotentCallStatusEnum item = getByValue(value);
        return Objects.nonNull(item) ? item.desc : null;
    }
}
