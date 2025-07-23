package com.itsoku.lesson012.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值记录表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/3 0:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_recharge")
@Data
public class RechargePO {
    //充值记录id
    private String id;

    //账户id
    private String accountId;

    //充值金额
    private BigDecimal price;

    //充值记录状态，0：处理中，1：充值成功
    private Integer status;

    //系统版本号，默认为0，每次更新+1，用于乐观锁
    private Long version;
}
