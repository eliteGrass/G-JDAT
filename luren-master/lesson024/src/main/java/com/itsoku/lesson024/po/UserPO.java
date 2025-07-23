package com.itsoku.lesson024.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_user_lesson024")
@Data
public class UserPO {
    //用户id
    private String id;
    //用户名
    private String name;
    //年龄
    private Integer age;
}
