package com.itsoku.lesson001.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson001.comm.ServiceExceptionUtils;
import com.itsoku.lesson001.config.ShardUploadConfig;
import com.itsoku.lesson001.dto.ShardUploadCompleteRequest;
import com.itsoku.lesson001.dto.ShardUploadDetailResponse;
import com.itsoku.lesson001.dto.ShardUploadInitRequest;
import com.itsoku.lesson001.dto.ShardUploadPartRequest;
import com.itsoku.lesson001.mapper.ShardUploadMapper;
import com.itsoku.lesson001.mapper.ShardUploadPartMapper;
import com.itsoku.lesson001.po.ShardUploadPO;
import com.itsoku.lesson001.po.ShardUploadPartPO;
import com.itsoku.lesson001.utils.IdUtils;
import com.itsoku.lesson001.utils.MergePerformanceMonitor;
import com.itsoku.lesson001.utils.ShardUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 21:03 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
@Service
public class ShardUploadServiceImpl extends ServiceImpl<ShardUploadMapper, ShardUploadPO> implements ShardUploadService {
    
    @Autowired
    private ShardUploadConfig shardUploadConfig;

    @Autowired
    private ShardUploadPartMapper shardUploadPartMapper;
    
    // 虚拟线程执行器（JDK 19+支持，向下兼容使用普通线程池）
    private final ExecutorService virtualThreadExecutor = createVirtualThreadExecutor();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String init(ShardUploadInitRequest request) {
        validateFileName(request.getFileName());
        
        ShardUploadPO po = new ShardUploadPO();
        po.setId(IdUtils.generateId());
        po.setFileName(request.getFileName());
        po.setPartNum(request.getPartNum());
        po.setMd5(request.getMd5());

        this.save(po);
        log.info("初始化分片上传任务成功，任务ID: {}, 文件名: {}, 分片数: {}", po.getId(), request.getFileName(), request.getPartNum());
        return po.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadPart(ShardUploadPartRequest request) throws IOException {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw ServiceExceptionUtils.exception("分片文件不能为空");
        }
        
        //如果分片已上传，则直接返回
        if (this.getUploadPartPO(request.getShardUploadId(), request.getPartOrder()) != null) {
            log.info("分片已存在，跳过上传: shardUploadId={}, partOrder={}", request.getShardUploadId(), request.getPartOrder());
            return;
        }
        
        //1、分片文件完整路径
        String partFileFullPath = this.getPartFileFullPath(request.getShardUploadId(), request.getPartOrder());
        Path partFilePath = Paths.get(partFileFullPath);
        
        // 确保目录存在
        Files.createDirectories(partFilePath.getParent());

        //2、将分片文件落入磁盘
        try {
            request.getFile().transferTo(partFilePath.toFile());
            log.info("分片文件上传成功: {}", partFileFullPath);
        } catch (IOException e) {
            log.error("分片文件上传失败: {}", partFileFullPath, e);
            throw ServiceExceptionUtils.exception("分片文件上传失败: " + e.getMessage());
        }

        //3、将分片文件信息写入db中
        this.saveShardUploadPart(request, partFileFullPath);
    }

    private ShardUploadPartPO getUploadPartPO(String shardUploadId, Integer partOrder) {
        LambdaQueryWrapper<ShardUploadPartPO> wq = Wrappers.lambdaQuery(ShardUploadPartPO.class)
                .eq(ShardUploadPartPO::getShardUploadId, shardUploadId)
                .eq(ShardUploadPartPO::getPartOrder, partOrder);
        return this.shardUploadPartMapper.selectOne(wq);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(ShardUploadCompleteRequest request) throws IOException {
        //1、获取分片任务 && 分片文件列表
        ShardUploadPO shardUploadPO = this.getById(request.getShardUploadId());
        if (shardUploadPO == null) {
            throw ServiceExceptionUtils.exception("分片任务不存在");
        }
        List<ShardUploadPartPO> shardUploadPartList = this.getShardUploadPartList(request.getShardUploadId());
        if (shardUploadPartList.size() != shardUploadPO.getPartNum()) {
            throw ServiceExceptionUtils.exception(String.format("分片还未上传完毕，已上传: %d，总计: %d", 
                shardUploadPartList.size(), shardUploadPO.getPartNum()));
        }

        //2、合并分片文件
        File file = this.mergeFile(shardUploadPO, shardUploadPartList);

        //3、将最终的文件信息写到db中
        shardUploadPO.setFileFullPath(file.getAbsolutePath());
        this.updateById(shardUploadPO);
        
        log.info("分片文件合并完成，任务ID: {}, 最终文件: {}", request.getShardUploadId(), file.getAbsolutePath());
    }

    @Override
    public ShardUploadDetailResponse detail(String shardUploadId) {
        ShardUploadPO shardUploadPO = this.getById(shardUploadId);
        if (shardUploadPO == null) {
            return null;
        }
        List<ShardUploadPartPO> shardUploadPartList = this.getShardUploadPartList(shardUploadId);

        ShardUploadDetailResponse response = new ShardUploadDetailResponse();
        response.setShardUploadId(shardUploadId);
        response.setPartNum(shardUploadPO.getPartNum());
        response.setSuccess(Objects.equals(shardUploadPO.getPartNum(), shardUploadPartList.size()));
        response.setPartOrderList(shardUploadPartList.stream().map(ShardUploadPartPO::getPartOrder).collect(Collectors.toList()));

        return response;
    }

    /**
     * 合并文件，返回最终文件（支持虚拟线程并发合并）
     *
     * @param shardUploadPO
     * @param shardUploadPartList
     * @return
     * @throws IOException
     */
    private File mergeFile(ShardUploadPO shardUploadPO, List<ShardUploadPartPO> shardUploadPartList) throws IOException {
        String finalFilePath = this.getFileFullName(shardUploadPO);
        Path finalPath = Paths.get(finalFilePath);
        
        // 确保目录存在
        Files.createDirectories(finalPath.getParent());
        
        // 如果最终文件已存在，删除它
        Files.deleteIfExists(finalPath);

        long startTime = System.currentTimeMillis();
        int partCount = shardUploadPartList.size();
        boolean useConcurrent = partCount >= shardUploadConfig.getConcurrentMergeThreshold();
        
        // 记录预期性能提升
        boolean isVirtualThread = isVirtualThreadSupported();
        String performanceExpectation = MergePerformanceMonitor.calculatePerformanceGain(partCount, isVirtualThread);
        log.info("开始文件合并 - {}", performanceExpectation);
        
        // 根据分片数量决定使用串行还是并发合并
        if (useConcurrent) {
            log.info("分片数量: {}，启用并发合并模式", partCount);
            mergeFileConcurrently(finalPath, shardUploadPartList);
        } else {
            log.info("分片数量: {}，使用串行合并模式", partCount);
            mergeFileSequentially(finalPath, shardUploadPartList);
        }

        long mergeTime = System.currentTimeMillis() - startTime;
        log.info("文件合并完成，耗时: {} ms，分片数: {}", mergeTime, partCount);

        File finalFile = finalPath.toFile();
        
        //校验合并后的文件和目标文件的md5是否一致
        long md5Time = 0;
        if (StringUtils.isNotBlank(shardUploadPO.getMd5())) {
            long md5StartTime = System.currentTimeMillis();
            String actualMd5 = SecureUtil.md5(finalFile);
            md5Time = System.currentTimeMillis() - md5StartTime;
            
            if (!shardUploadPO.getMd5().equals(actualMd5)) {
                // 删除损坏的文件
                Files.deleteIfExists(finalPath);
                throw ServiceExceptionUtils.exception(String.format("文件md5不匹配，期望: %s，实际: %s", 
                    shardUploadPO.getMd5(), actualMd5));
            }
            log.info("文件MD5校验通过: {}，校验耗时: {} ms", actualMd5, md5Time);
        }
        
        // 记录性能指标
        MergePerformanceMonitor.logPerformanceMetrics(partCount, mergeTime, md5Time, useConcurrent);
        
        return finalFile;
    }

    /**
     * 串行合并文件分片
     */
    private void mergeFileSequentially(Path finalPath, List<ShardUploadPartPO> shardUploadPartList) throws IOException {
        try (FileChannel outputChannel = FileChannel.open(finalPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            for (ShardUploadPartPO part : shardUploadPartList) {
                mergePartFile(outputChannel, part);
            }
        }
    }

    /**
     * 并发合并文件分片（使用虚拟线程）
     */
    private void mergeFileConcurrently(Path finalPath, List<ShardUploadPartPO> shardUploadPartList) throws IOException {
        // 将分片列表按批次分组
        List<List<ShardUploadPartPO>> batches = partitionList(shardUploadPartList, shardUploadConfig.getMergeBatchSize());
        
        try (FileChannel outputChannel = FileChannel.open(finalPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            // 为每个批次创建临时文件并并发处理
            List<CompletableFuture<Path>> batchFutures = new ArrayList<>();
            
            for (int i = 0; i < batches.size(); i++) {
                final int batchIndex = i;
                final List<ShardUploadPartPO> batch = batches.get(i);
                
                CompletableFuture<Path> batchFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return mergeBatchToTempFile(batch, batchIndex);
                    } catch (IOException e) {
                        log.error("批次 {} 合并失败", batchIndex, e);
                        throw new RuntimeException("批次合并失败: " + e.getMessage(), e);
                    }
                }, virtualThreadExecutor);
                
                batchFutures.add(batchFuture);
            }
            
            // 等待所有批次完成并按顺序合并到最终文件
            for (int i = 0; i < batchFutures.size(); i++) {
                try {
                    Path tempBatchFile = batchFutures.get(i).join();
                    
                    // 将批次临时文件合并到最终文件
                    try (FileChannel batchChannel = FileChannel.open(tempBatchFile, StandardOpenOption.READ)) {
                        long size = batchChannel.size();
                        long position = 0;
                        
                        while (position < size) {
                            long transferred = batchChannel.transferTo(position, size - position, outputChannel);
                            position += transferred;
                        }
                        log.debug("批次 {} 合并到最终文件完成，大小: {} bytes", i, size);
                    }
                    
                    // 删除临时文件
                    Files.deleteIfExists(tempBatchFile);
                    
                } catch (Exception e) {
                    log.error("合并批次 {} 到最终文件失败", i, e);
                    throw new IOException("并发合并失败: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 将一个批次的分片合并到临时文件
     */
    private Path mergeBatchToTempFile(List<ShardUploadPartPO> batch, int batchIndex) throws IOException {
        Path tempFile = Files.createTempFile("shard-batch-" + batchIndex + "-", ".tmp");
        
        try (FileChannel outputChannel = FileChannel.open(tempFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            for (ShardUploadPartPO part : batch) {
                mergePartFile(outputChannel, part);
            }
        }
        
        log.debug("批次 {} 合并完成，临时文件: {}，包含分片: {}", 
                batchIndex, tempFile, 
                batch.stream().map(ShardUploadPartPO::getPartOrder).collect(Collectors.toList()));
        
        return tempFile;
    }

    /**
     * 合并单个分片文件到输出通道
     */
    private void mergePartFile(FileChannel outputChannel, ShardUploadPartPO part) throws IOException {
        Path partPath = Paths.get(part.getFileFullPath());
        if (!Files.exists(partPath)) {
            throw ServiceExceptionUtils.exception("分片文件不存在: " + part.getFileFullPath());
        }
        
        try (FileChannel inputChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
            long size = inputChannel.size();
            long position = 0;
            
            while (position < size) {
                long transferred = inputChannel.transferTo(position, size - position, outputChannel);
                position += transferred;
            }
            log.debug("合并分片 {} 完成，大小: {} bytes", part.getPartOrder(), size);
        }
        
        // 删除分片文件
        try {
            Files.delete(partPath);
            log.debug("删除分片文件: {}", part.getFileFullPath());
        } catch (IOException e) {
            log.warn("删除分片文件失败: {}", part.getFileFullPath(), e);
        }
    }

    /**
     * 创建虚拟线程执行器（兼容不同JDK版本）
     */
    private ExecutorService createVirtualThreadExecutor() {
        try {
            // 尝试使用JDK 21+的虚拟线程
            return (ExecutorService) Executors.class.getMethod("newVirtualThreadPerTaskExecutor").invoke(null);
        } catch (Exception e) {
            // 如果不支持虚拟线程，降级到普通线程池
            log.info("虚拟线程不可用，使用普通线程池");
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }
    
    /**
     * 检测是否支持虚拟线程
     */
    private boolean isVirtualThreadSupported() {
        try {
            Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * 将列表按指定大小分组
     */
    private <T> List<List<T>> partitionList(List<T> list, int partitionSize) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += partitionSize) {
            partitions.add(list.subList(i, Math.min(i + partitionSize, list.size())));
        }
        return partitions;
    }

    /**
     * 获取分片文件列表（并按顺序排序号）
     *
     * @param shardUploadId
     * @return
     */
    private List<ShardUploadPartPO> getShardUploadPartList(String shardUploadId) {
        return this.shardUploadPartMapper.selectList(Wrappers.lambdaQuery(ShardUploadPartPO.class).eq(ShardUploadPartPO::getShardUploadId, shardUploadId).orderByAsc(ShardUploadPartPO::getPartOrder));
    }

    private ShardUploadPartPO saveShardUploadPart(ShardUploadPartRequest request, String partFileFullPath) {
        ShardUploadPartPO partPO = new ShardUploadPartPO();
        partPO.setId(IdUtils.generateId());
        partPO.setShardUploadId(request.getShardUploadId());
        partPO.setPartOrder(request.getPartOrder());
        partPO.setFileFullPath(partFileFullPath);
        this.shardUploadPartMapper.insert(partPO);
        return partPO;
    }

    private String getPartFileFullPath(String shardUploadId, Integer partOrder) {
        validateShardUploadId(shardUploadId);
        return Paths.get(shardUploadConfig.getFilePath(), shardUploadId, String.valueOf(partOrder)).toString();
    }

    private String getFileFullName(ShardUploadPO shardUploadPO) {
        validateShardUploadId(shardUploadPO.getId());
        validateFileName(shardUploadPO.getFileName());
        return Paths.get(shardUploadConfig.getFilePath(), shardUploadPO.getId(), shardUploadPO.getFileName()).toString();
    }

    /**
     * 验证文件名安全性，防止路径遍历攻击
     */
    private void validateFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw ServiceExceptionUtils.exception("文件名不能为空");
        }
        
        // 检查危险字符
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") 
            || fileName.contains(":") || fileName.contains("*") || fileName.contains("?") 
            || fileName.contains("\"") || fileName.contains("<") || fileName.contains(">") 
            || fileName.contains("|")) {
            throw ServiceExceptionUtils.exception("文件名包含非法字符");
        }
        
        // 检查文件名长度
        if (fileName.length() > 200) {
            throw ServiceExceptionUtils.exception("文件名过长");
        }
    }
    
    /**
     * 验证分片任务ID安全性
     */
    private void validateShardUploadId(String shardUploadId) {
        if (StringUtils.isBlank(shardUploadId)) {
            throw ServiceExceptionUtils.exception("分片任务ID不能为空");
        }
        
        // 只允许字母数字和连字符
        if (!shardUploadId.matches("^[a-zA-Z0-9-]+$")) {
            throw ServiceExceptionUtils.exception("分片任务ID格式不正确");
        }
        
        if (shardUploadId.length() > 64) {
            throw ServiceExceptionUtils.exception("分片任务ID过长");
        }
    }

    /**
     * 应用关闭时优雅关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (virtualThreadExecutor != null && !virtualThreadExecutor.isShutdown()) {
            log.info("正在关闭虚拟线程执行器...");
            virtualThreadExecutor.shutdown();
            try {
                if (!virtualThreadExecutor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                    log.warn("线程池未能在30秒内关闭，强制关闭");
                    virtualThreadExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.warn("等待线程池关闭时被中断", e);
                virtualThreadExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

}
