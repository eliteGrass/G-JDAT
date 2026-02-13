package com.liteGrass.bm.speedTest.properties;


import lombok.Data;

/**
 * @ClassName: SpeedTestProperties
 * @Author: liteGrass
 * @Date: 2025/10/29 9:49
 * @Description: 配置参数
 */
@Data
public class SpeedTestProperties {
    // 下载测试最大文件大小（MB）
    private int downloadMaxSizeMb = 10;
    // 上传测试最大文件大小（MB）
    private int uploadMaxSizeMb = 5;
    // 单IP单位时间内最大请求数
    private int maxRequestsPerIp = 5;
    // 频率限制时间窗口（秒）
    private int rateLimitPeriodSeconds = 60;
}
