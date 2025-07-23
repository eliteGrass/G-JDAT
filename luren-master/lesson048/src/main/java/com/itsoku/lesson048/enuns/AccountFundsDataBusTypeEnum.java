package com.itsoku.lesson048.enuns;

/**
 * 资金流水类型
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 12:34 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum AccountFundsDataBusTypeEnum {
    CASH_OUT_FROZEN(1001, "提现冻结", AccountFundsDataIncomeEnum.FIXED),
    CASH_OUT_SUCCESS(1002, "提现成功", AccountFundsDataIncomeEnum.OUT),
    CASH_OUT_FAIL(1003, "提现失败解冻", AccountFundsDataIncomeEnum.FIXED),
    ;

    AccountFundsDataBusTypeEnum(Integer value, String description, AccountFundsDataIncomeEnum income) {
        this.value = value;
        this.description = description;
        this.income = income;
    }

    private Integer value;
    private String description;

    private AccountFundsDataIncomeEnum income;

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public AccountFundsDataIncomeEnum getIncome() {
        return income;
    }
}
