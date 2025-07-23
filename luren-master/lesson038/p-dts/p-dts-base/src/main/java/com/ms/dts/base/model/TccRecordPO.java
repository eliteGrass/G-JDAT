package com.ms.dts.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.itsoku.common.po.BasePO;
import lombok.*;

/**
 * <b>description</b>：tcc分布式事务记录表 <br>
 * <b>time</b>：2019-02-22 16:47:47 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("t_tcc_record")
public class TccRecordPO extends BasePO {
    /**
     * 编号
     */
    private String id;
    /**
     * 业务类型
     */
    private Integer busType;
    /**
     * 业务id
     */
    private String busId;
    /**
     * 事务发起者
     */
    private String classname;
    /**
     * 状态，0：待处理，1：处理成功，2：处理失败
     */
    private Integer status;
    /**
     * 补偿的servicename
     */
    private String serviceApplicationName;
    /**
     * 补偿的beanname
     */
    private String beanname;
    /**
     * 请求信息，json格式
     */
    private String requestData;
    /**
     * 下次处理时间
     */
    private Long nextDisposeTime;
    /**
     * 最大允许失败次数
     */
    private Integer maxFailure;
    /**
     * 当前失败次数
     */
    private Integer failure;
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