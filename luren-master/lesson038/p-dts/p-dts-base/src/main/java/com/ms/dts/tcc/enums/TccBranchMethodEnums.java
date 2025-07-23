package com.ms.dts.tcc.enums;

import com.ms.dts.tcc.branch.ITccBranch;

import java.util.Objects;

/**
 * <b>description</b>：tcc事务处理相状态结果 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum TccBranchMethodEnums {
    try1(0, ITccBranch.TRY1_METHOD),
    confirm(1, ITccBranch.CONFIRM_METHOD),
    cancel(2, ITccBranch.CANCEL_METHOD);
    private Integer value;
    private String methodName;

    TccBranchMethodEnums(Integer value, String methodName) {
        this.value = value;
        this.methodName = methodName;
    }

    public Integer getValue() {
        return value;
    }

    public String getMethodName() {
        return methodName;
    }

    public static TccBranchMethodEnums getByValue(Integer value) {
        TccBranchMethodEnums result = null;
        for (TccBranchMethodEnums item : TccBranchMethodEnums.values()) {
            if (item.getValue().equals(value)) {
                result = item;
                break;
            }
        }
        return result;
    }

    public static TccBranchMethodEnums getByMethodName(String methodName) {
        TccBranchMethodEnums result = null;
        for (TccBranchMethodEnums item : TccBranchMethodEnums.values()) {
            if (item.getMethodName().equals(methodName)) {
                result = item;
                break;
            }
        }
        return result;
    }

    public static String getMethodName(Integer value) {
        TccBranchMethodEnums enums = getByValue(value);
        return Objects.nonNull(enums) ? enums.getMethodName() : null;
    }

}
