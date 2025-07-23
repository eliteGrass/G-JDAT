package com.itsoku.lesson048.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_cash_out_lesson048")
@Data
public class CashOutPO {
    //提现记录id
    @TableId(type = IdType.AUTO)
    private Long id;

    //账号id
    private Long accountId;

    //提现金额
    private BigDecimal price;

    //状态，0：待处理，100：提现成功，200：提现失败
    private Integer status;

    //失败原因
    private String failMsg;

    //创建时间
    private LocalDateTime createTime;

    //最后更新时间
    private LocalDateTime updateTime;
}
