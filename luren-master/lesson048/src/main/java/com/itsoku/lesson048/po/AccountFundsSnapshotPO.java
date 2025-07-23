package com.itsoku.lesson048.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资金账户快照
 */
@Data
@TableName("t_account_funds_snapshot_lesson048")
public class AccountFundsSnapshotPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号id
     */
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
     * 创建时间
     */
    protected LocalDateTime createTime;
}
