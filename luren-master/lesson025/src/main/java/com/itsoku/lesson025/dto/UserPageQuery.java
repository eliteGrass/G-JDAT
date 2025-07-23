package com.itsoku.lesson025.dto;

import com.itsoku.lesson025.page.PageQuery;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 21:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class UserPageQuery extends PageQuery {
    //根据用户名模糊匹配
    private String keyword;
}
