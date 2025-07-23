package com.itsoku.lesson037.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson037.po.AccountPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface AccountMapper extends BaseMapper<AccountPO> {
    /**
     * 减少账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    @Update("update t_account_lesson037 set balance = balance - #{price} where id = #{accountId} and balance - #{price} >=0 and #{price}>0")
    int balanceSubtract(@Param("accountId") String accountId, @Param("price") BigDecimal price);
}
