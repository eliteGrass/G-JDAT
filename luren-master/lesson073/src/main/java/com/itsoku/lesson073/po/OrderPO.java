package com.itsoku.lesson073.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 21:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order_073")
public class OrderPO {
    /**
     * 订单id
     */
    @TableId
    private String id;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品数量
     */
    private Integer num;
}
