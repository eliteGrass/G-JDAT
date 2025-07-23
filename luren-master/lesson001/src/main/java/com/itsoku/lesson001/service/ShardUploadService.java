package com.itsoku.lesson001.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson001.dto.ShardUploadCompleteRequest;
import com.itsoku.lesson001.dto.ShardUploadDetailResponse;
import com.itsoku.lesson001.dto.ShardUploadInitRequest;
import com.itsoku.lesson001.dto.ShardUploadPartRequest;
import com.itsoku.lesson001.po.ShardUploadPO;

import java.io.IOException;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 21:03 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ShardUploadService extends IService<ShardUploadPO> {
    /**
     * 创建分片上传任务
     *
     * @param request
     * @return 分片任务id
     */
    String init(ShardUploadInitRequest request);

    /**
     * 上传分片
     *
     * @param request
     */
    void uploadPart(ShardUploadPartRequest request) throws IOException;

    /**
     * 完成分片上传，合并分片文件
     *
     * @param request
     */
    void complete(ShardUploadCompleteRequest request) throws IOException;

    /**
     * 获取分片任务详细信息
     *
     * @param shardUploadId 分片任务id
     * @return
     */
    ShardUploadDetailResponse detail(String shardUploadId);
}
