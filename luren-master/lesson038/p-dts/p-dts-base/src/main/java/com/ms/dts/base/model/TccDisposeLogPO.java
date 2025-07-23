package com.ms.dts.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itsoku.common.po.BasePO;
import lombok.*;

/**
 * <b>description</b>：tcc分布式事务->补偿日志 <br>
 * <b>time</b>：2019-04-13 11:21:49 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("t_tcc_dispose_log")
public class TccDisposeLogPO extends BasePO {
    /**
     * 编号
     */
    private String id;
    /**
     * 事务记录id发，来源于t_tcc_record表的id
     */
    private String tccRecordId;
    /**
     * 开始时间（时间戳）
     */
    private Long starttime;
    /**
     * 结束时间（时间戳）
     */
    private Long endtime;
    /**
     * 执行结果
     */
    private String msg;
    /**
     * 创建时间
     */
    private Long addtime;
}