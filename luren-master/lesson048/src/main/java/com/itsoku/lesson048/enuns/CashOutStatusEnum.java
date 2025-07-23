package com.itsoku.lesson048.enuns;

/**
 * <b>description</b>：提现记录状态 <br>
 * <b>time</b>： 16:30 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum CashOutStatusEnum {
    PROCESSING(0, "处理中"),
    SUCCESS(1, "提现打款成功"),
    FAIL(2, "提现打款失败"),
    ;

    private Integer value;
    private String desc;

    CashOutStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
