package com.itsoku.lesson001.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 22:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class ShardUploadInitRequest {
    /**
     * 文件名称
     */

    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    /**
     * 分片数量
     */
    @NotNull(message = "分片数量不能为空")
    @Min(value = 1, message = "分片数量必须大于0")
    private Integer partNum;

    /**
     * 文件md5字
     */
    private String md5;

}
