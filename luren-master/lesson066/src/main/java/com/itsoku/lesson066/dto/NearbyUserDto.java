package com.itsoku.lesson066.dto;

import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/18 20:23 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class NearbyUserDto {
    //用户id
    private String userId;
    //经度
    private Double longitude;
    //纬度
    private Double latitude;
    //距离（米）
    private Double distance;
}
