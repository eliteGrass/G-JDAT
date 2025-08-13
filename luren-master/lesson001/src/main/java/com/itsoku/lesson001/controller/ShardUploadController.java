package com.itsoku.lesson001.controller;

import com.itsoku.lesson001.comm.Result;
import com.itsoku.lesson001.comm.ResultUtils;
import com.itsoku.lesson001.dto.ShardUploadCompleteRequest;
import com.itsoku.lesson001.dto.ShardUploadDetailResponse;
import com.itsoku.lesson001.dto.ShardUploadInitRequest;
import com.itsoku.lesson001.dto.ShardUploadPartRequest;
import com.itsoku.lesson001.service.ShardUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 21:02 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/shardUpload")
@Validated
public class ShardUploadController {
    @Autowired
    private ShardUploadService shardUploadService;

    /**
     * 创建分片上传任务
     *
     * @return 分片任务id
     */
    @PostMapping("/init")
    public Result<String> init(@Valid @RequestBody ShardUploadInitRequest request) {
        String shardUploadId = this.shardUploadService.init(request);
        return ResultUtils.ok(shardUploadId);
    }

    /**
     * 上传分片（客户端需遍历上传分片文件）
     *
     * @return
     */
    @PostMapping("/uploadPart")
    public Result<Boolean> uploadPart(@Valid ShardUploadPartRequest request) throws IOException {
        this.shardUploadService.uploadPart(request);
        return ResultUtils.ok(true);
    }

    /**
     * 合并分片，完成上传
     *
     * @return
     */
    @PostMapping("/complete")
    public Result<Boolean> complete(@Valid @RequestBody ShardUploadCompleteRequest request) throws IOException {
        this.shardUploadService.complete(request);
        return ResultUtils.ok(true);
    }

    /**
     * 获取分片任务详细信息
     *
     * @param shardUploadId 分片任务id
     * @return
     */
    @GetMapping("/detail")
    public Result<ShardUploadDetailResponse> detail(@RequestParam("shardUploadId") @NotBlank(message = "分片任务ID不能为空") String shardUploadId) {
        return ResultUtils.ok(this.shardUploadService.detail(shardUploadId));
    }
}
