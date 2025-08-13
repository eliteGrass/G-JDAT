package com.itsoku.lesson001.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 分片上传配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shard-upload")
public class ShardUploadConfig {
    
    /**
     * 文件存储路径
     */
    private String filePath = "./uploads/";
    
    /**
     * 分片缓冲区大小（默认1MB）
     */
    private int chunkSize = 1024 * 1024;
    
    /**
     * 启用并发合并的最小分片数量阈值（默认10个分片）
     */
    private int concurrentMergeThreshold = 10;
    
    /**
     * 并发合并时的批次大小（默认5个分片一批）
     */
    private int mergeBatchSize = 5;
}