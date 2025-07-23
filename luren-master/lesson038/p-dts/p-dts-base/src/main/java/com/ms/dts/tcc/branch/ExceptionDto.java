package com.ms.dts.tcc.branch;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * <b>description</b>：异常信息 <br>
 * <b>time</b>：2024/4/31 9:19 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExceptionDto implements Serializable {
    /**
     * 错误代码
     */
    private String code;
    /**
     * 子代码
     */
    private String subCode;
    /**
     * 信息提示
     */
    private String msg;
    /**
     * 扩展数据
     */
    private Map extData;

    /**
     * 异常栈信息
     */
    private String throwableStack;

}
