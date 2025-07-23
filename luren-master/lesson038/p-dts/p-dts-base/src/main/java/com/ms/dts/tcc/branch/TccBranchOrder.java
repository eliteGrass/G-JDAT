package com.ms.dts.tcc.branch;

import java.lang.annotation.*;

/**
 * <b>description</b>：branch在业务中的顺序，放在字段上面 <br>
 * <b>time</b>：2024/4/28 15:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TccBranchOrder {
    int value();
}
