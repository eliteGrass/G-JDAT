package com.itsoku.lesson077;

import lombok.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/22 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuPO {
    //菜单id
    private String id;

    //名称
    private String name;

    //父id
    private String pid;

    //同级排序
    private Integer theSort;

    //url
    private String url;
}
