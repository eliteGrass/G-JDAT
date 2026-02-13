package com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @EnumName: Select
 * @Author: liteGrass
 * @Date: 2025/11/4 16:43
 * @Description: 查询注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Insert {
    String value() default "";
}
