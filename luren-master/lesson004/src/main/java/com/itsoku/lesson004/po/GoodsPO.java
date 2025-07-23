package com.itsoku.lesson004.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 21:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@TableName("t_goods")
public class GoodsPO {
    /**
     * 商品id
     */
    @TableId
    private String goodsId;

    /**
     * 文件名称
     */

    private String goodsName;

    /**
     * 商品库存
     */
    private Integer num;

    /**
     * 版本号
     */
    private Long version;
}
