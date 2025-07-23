package com.itsoku.lesson012.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson012.po.AccountPO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface AccountMapper extends BaseMapper<AccountPO> {
    int balanceAdd(@Param("accountId") String accountId, @Param("price") BigDecimal price);
}
