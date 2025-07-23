package com.itsoku.lesson046.po;

import com.itsoku.orm.IdType;
import com.itsoku.orm.annotation.Table;
import com.itsoku.orm.annotation.TableField;
import com.itsoku.orm.annotation.TableId;
import lombok.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 19:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("t_user_lesson046")
public class UserPO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_name")
    private String userName;

    private Integer age;
}
