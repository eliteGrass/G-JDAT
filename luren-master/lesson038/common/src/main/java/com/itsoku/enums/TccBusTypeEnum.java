package com.itsoku.enums;

import java.util.Objects;

public enum TccBusTypeEnum {

    TRANSFER(1, "跨库转账"),
    ;

    private int code;
    private String description;

    TccBusTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TccBusTypeEnum parse(int code) {
        for (TccBusTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("非法的TCC业务类型");
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDesc(int code) {
        TccBusTypeEnum enums = parse(code);
        return Objects.nonNull(enums) ? enums.getDescription() : null;
    }
}
