package com.itsoku.lesson012.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户表
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_account")
@Data
public class AccountPO {
    //账户id
    private String id;

    //账户名
    private String name;

    //账户余额
    private BigDecimal balance;
}
