package com.itsoku.lesson078.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/23 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    //姓名
    private String name;
    //头像
    private MultipartFile headImg;
    //多张证件照
    private List<MultipartFile> idImgList;
}
