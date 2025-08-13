package com.itsoku.lesson001.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 22:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class ShardUploadPartRequest {
    /**
     * 分片上传任务id（由初始化分片接口返回的）
     */
    @NotBlank(message = "分片任务ID不能为空")
    private String shardUploadId;
    /**
     * 第几个分片
     */
    @NotNull(message = "分片序号不能为空")
    @Min(value = 1, message = "分片序号必须大于0")
    private Integer partOrder;

    /**
     * 分片文件
     */
    @NotNull(message = "分片文件不能为空")
    private MultipartFile file;
}
