package com.itsoku.lesson004.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson004.po.GoodsPO;
import org.apache.ibatis.annotations.Param;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/29 11:17 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface GoodsMapper extends BaseMapper<GoodsPO> {
    /**
     * 下单
     *
     * @param goodsId 商品id
     * @param num     商品数量
     * @return
     */
    int placeOrder1(@Param("goodsId") String goodsId, @Param("num") int num);

    /**
     * 下单
     *
     * @param goodsId       商品id
     * @param num           商品数量
     * @param expectVersion version 期望值
     * @return
     */
    int placeOrder2(@Param("goodsId") String goodsId, @Param("num") int num, @Param("expectVersion") long expectVersion);

    /**
     * 下单
     *
     * @param goodsId 商品id
     * @param num     商品数量
     * @return
     */
    int placeOrder3(@Param("goodsId") String goodsId, @Param("num") int num);
}
