package com.ms.dts.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.itsoku.common.po.BasePO;
import lombok.*;

/**
 * <b>description</b>：业务库中分支事务日志记录 <br>
 * <b>time</b>：2019-01-29 17:35:36 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("t_tcc_bus_branch_log")
public class TccBusBranchLogPO extends BasePO {
    /**
     * 编号
     */
    private String id;
    /**
     * 事务记录id发，来源于t_tcc_record表的id
     */
    private String tccRecordId;
    /**
     * 分支完整类名
     */
    private String classname;
    /**
     * 方法，0:try1，1:confirm，2:cancel
     */
    private Integer method;
    /**
     * 分支响应结果
     */
    private String msg;
    /**
     * 状态，0：待处理，1：处理成功，2：处理失败
     */
    private Integer status;
    /**
     * 上下文信息,json格式
     */
    private String context;
    /**
     * 创建时间
     */
    private Long addtime;
    /**
     * 最后更新时间
     */
    private Long uptime;
    /**
     * 版本号，默认为0，每次更新+1
     */
    @Version
    private Long version;
}