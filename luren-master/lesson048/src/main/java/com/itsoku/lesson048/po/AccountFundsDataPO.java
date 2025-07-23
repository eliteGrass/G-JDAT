package com.itsoku.lesson048.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户资金账户流水
 */
@Data
@TableName("t_account_funds_data_lesson048")
public class AccountFundsDataPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号id
     */
    private Long accountId;

    /**
     * 交易金额
     */
    private BigDecimal price;

    /**
     * 本交易前资金快照id，快照是指交易时将（account_funds）当时的记录备份一份
     *
     * @see AccountFundsSnapshotPO#getId()
     */
    private Long beforeAccountFundsSnapshotId;

    /**
     * 本交易后资金快照id，快照是指交易后将（account_funds）当时的记录备份一份
     *
     * @see AccountFundsSnapshotPO#getId()
     */
    private Long afterAccountFundsSnapshotId;

    /**
     * 进出标志位，0：账户总金额余额不变，1：账户总金额增加，-1：账户总余额减少 （总金额 = account_funds.balance+ account_funds.frozen）
     */
    private Integer income;

    /**
     * 业务关联的业务类型
     */
    private Integer busType;

    /**
     * 业务关联的业务id
     */
    private Long busId;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
}
