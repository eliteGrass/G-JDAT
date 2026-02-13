package com.liteGrass.bm.speedTest.controller;


import com.liteGrass.bm.speedTest.properties.SpeedTestProperties;
import com.liteGrass.bm.speedTest.service.SpeedTestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @ClassName: SpeedTestController
 * @Author: liteGrass
 * @Date: 2025/10/29 9:41
 * @Description: 测速接口
 */
@RestController
@RequestMapping("/api/speed-test")
public class SpeedTestController {

    @Autowired
    private SpeedTestService speedTestService;

    @Autowired
    private SpeedTestProperties properties;

    /**
     * 下载测试接口
     */
    @GetMapping("/download")
    public ResponseEntity<InputStream> downloadTest(
            @RequestParam int size,
            HttpServletResponse response,
            @RequestHeader(value = "X-Forwarded-For", defaultValue = "unknown") String clientIp) {

        // 1. 频率限制检查
        if (!speedTestService.checkRateLimit(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        // 2. 校验文件大小（防止恶意请求过大文件）
        if (size <= 0 || size > properties.getDownloadMaxSizeMb()) {
            return ResponseEntity.badRequest().build();
        }

        // 3. 禁用缓存（避免影响测试准确性）
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");

        // 4. 返回随机数据
        InputStream data = speedTestService.generateDownloadData(size);
        return ResponseEntity.ok(data);
    }

    /**
     * 上传测试接口
     */
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadTest(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-Forwarded-For", defaultValue = "unknown") String clientIp) {

        // 1. 频率限制检查
        if (!speedTestService.checkRateLimit(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        // 2. 校验上传文件
        if (file.isEmpty() || !speedTestService.validateUploadFile(file)) {
            return ResponseEntity.badRequest().build();
        }

        // 3. 无需存储文件，仅确认接收完成
        return ResponseEntity.ok().build();
    }

    /**
     * 延迟测试接口（轻量化，仅返回空响应）
     */
    @GetMapping("/ping")
    public ResponseEntity<Void> pingTest() {
        // 不做任何业务逻辑，直接返回，减少服务端耗时影响
        return ResponseEntity.ok().build();
    }
}
