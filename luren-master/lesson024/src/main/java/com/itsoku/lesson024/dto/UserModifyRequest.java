package com.itsoku.lesson024.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class UserModifyRequest {
    @NotBlank(message = "用户id不能为空")
    private String id;

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄最小1岁")
    @Max(value = 100, message = "年龄最大100岁")
    private Integer age;
}
