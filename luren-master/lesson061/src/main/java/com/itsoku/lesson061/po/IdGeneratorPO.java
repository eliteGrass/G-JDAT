package com.itsoku.lesson061.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/6 13:55 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@TableName("t_id_generator_lesson061")
public class IdGeneratorPO {
    //编码，唯一
    @TableId
    private String code;

    //当前最大id
    private Long maxId;
}
