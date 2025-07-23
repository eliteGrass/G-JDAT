package com.itsoku.lesson007.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/31 22:03 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_lesson007")
@Data
public class Lesson007PO {
    private String id;
    private String data;
}
