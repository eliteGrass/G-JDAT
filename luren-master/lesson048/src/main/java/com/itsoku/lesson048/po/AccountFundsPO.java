package com.itsoku.lesson048.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户资金账户
 */
@Data
@TableName("t_account_funds_lesson048")
public class AccountFundsPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号id
     */
    @TableId
    private Long accountId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozen;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
