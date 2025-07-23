package com.itsoku.lesson048.enuns;

/**
 * <b>description</b>：资金流水导致余额变动的标志 <br>
 * <b>time</b>：2024/6/13 12:34 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum AccountFundsDataIncomeEnum {
    IN(1, "收入"),
    FIXED(0, "不变"),
    OUT(-1, "支出");

    AccountFundsDataIncomeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    private Integer value;
    private String description;

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
