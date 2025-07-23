package com.itsoku.lesson038.service2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson038.service2.po.AccountPO;
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
     * 增加账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    @Update("update t_account_lesson038 set balance = balance + #{price} where id = #{accountId} and #{price}>0")
    int balanceAdd(@Param("accountId") String accountId, @Param("price") BigDecimal price);

    /**
     * 减少账户余额
     *
     * @param accountId
     * @param price
     * @return
     */
    @Update("update t_account_lesson038 set balance = balance - #{price} where id = #{accountId} and balance - #{price} >=0 and #{price}>0")
    int balanceSubtract(@Param("accountId") String accountId, @Param("price") BigDecimal price);


    /**
     * 冻结：将余额转入冻结金额中（balance-price，frozen+price）
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    @Update("update t_account_lesson038 set balance = balance - #{price},frozen = frozen + #{price}  where id = #{accountId} and balance - #{price}>=0 and #{price}>0")
    int frozen(String accountId, BigDecimal price);

    /**
     * 解冻：将冻结金额转入余额中（frozen-price，balance+price）
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    @Update("update t_account_lesson038 set frozen = frozen - #{price},balance = balance + #{price}  where id = #{accountId} and frozen - #{price}>=0 and #{price}>0")
    int unFrozen(String accountId, BigDecimal price);

    /**
     * 冻结金额增加price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    @Update("update t_account_lesson038 set frozen = frozen + #{price} where id = #{accountId} and #{price}>0")
    int frozenAdd(String accountId, BigDecimal price);

    /**
     * 冻结金额减少price，写入流水
     *
     * @param accountId     用户账号id
     * @param price         金额
     */
    @Update("update t_account_lesson038 set frozen = frozen - #{price} where id = #{accountId} and frozen - #{price} >=0 and #{price}>0")
    int frozenSubtract(String accountId, BigDecimal price);
}
