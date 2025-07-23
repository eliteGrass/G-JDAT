package com.ms.dts.base;

import java.util.Objects;

/**
 * <b>description</b>：tcc事务处理相状态结果 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum ResultStatus {
    INIT(0, "处理中"),
    SUCCESS(1, "处理成功"),
    FAIL(2, "处理失败");
    private Integer status;
    private String msg;

    ResultStatus(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static ResultStatus resultStatus(Integer status) {
        ResultStatus result = null;
        for (ResultStatus item : ResultStatus.values()) {
            if (item.getStatus().equals(status)) {
                result = item;
                break;
            }
        }
        return result;
    }

    public static String getDesc(Integer status) {
        ResultStatus result = resultStatus(status);
        return Objects.nonNull(result) ? result.getMsg() : null;
    }

    public static boolean isInit(Integer status) {
        return INIT.getStatus().equals(status);
    }
}
