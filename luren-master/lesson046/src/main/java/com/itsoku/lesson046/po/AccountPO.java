package com.itsoku.lesson046.po;

import com.itsoku.orm.annotation.Table;
import com.itsoku.orm.annotation.TableId;
import com.itsoku.orm.annotation.Version;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table("t_account_lesson046")
public class AccountPO{
    //用户id
    @TableId
    private String id;

    //用户名
    private String name;

    //价格
    private BigDecimal balance;

    //版本号
    @Version
    private Long version;
}
