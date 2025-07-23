package com.itsoku.lesson006;

import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/31 15:18 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class GoodsDetailResponse {
    //商品基本信息
    private String goodsInfo;

    // 商品描述信息
    private String goodsDescription;

    // 商品评论量
    private int commentCount;

    // 收藏量
    private int favoriteCount;
}
