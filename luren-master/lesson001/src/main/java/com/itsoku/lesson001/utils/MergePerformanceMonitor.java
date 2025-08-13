package com.itsoku.lesson001.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件合并性能监控工具
 */
@Slf4j
public class MergePerformanceMonitor {
    
    /**
     * 计算预期性能提升
     * 
     * @param partCount 分片数量
     * @param isVirtualThread 是否使用虚拟线程
     * @return 性能提升描述
     */
    public static String calculatePerformanceGain(int partCount, boolean isVirtualThread) {
        if (partCount < 10) {
            return "分片数量较少，串行处理效率更高";
        }
        
        if (isVirtualThread) {
            double expectedImprovement = Math.min(partCount / 5.0 * 0.6, 0.8); // 最多80%提升
            return String.format("预期性能提升: %.1f%%（虚拟线程并发处理）", expectedImprovement * 100);
        } else {
            double expectedImprovement = Math.min(partCount / 10.0 * 0.4, 0.5); // 最多50%提升
            return String.format("预期性能提升: %.1f%%（普通线程池并发处理）", expectedImprovement * 100);
        }
    }
    
    /**
     * 记录合并性能指标
     */
    public static void logPerformanceMetrics(int partCount, long mergeTime, long md5Time, boolean concurrent) {
        String mode = concurrent ? "并发" : "串行";
        double throughput = (double) partCount / (mergeTime / 1000.0); // 分片/秒
        
        log.info("=== 文件合并性能指标 ===");
        log.info("合并模式: {}", mode);
        log.info("分片数量: {}", partCount);
        log.info("合并耗时: {} ms", mergeTime);
        log.info("MD5校验耗时: {} ms", md5Time);
        log.info("总耗时: {} ms", mergeTime + md5Time);
        log.info("处理吞吐量: {:.2f} 分片/秒", throughput);
        log.info("========================");
    }
}