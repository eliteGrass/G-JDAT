package com.liteGrass.bm.likes.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: BmLikes
 * @Author: liteGrass
 * @Date: 2025/8/22 22:22
 * @Description: 点赞数量
 */
@Data
@TableName("like_count")
public class BmLikeCount {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 业务类型：1=文章 2=评论 3=视频 … */
    private Integer bizType;

    /** 业务主键，如文章ID */
    private Long bizId;

    /** 点赞总数 */
    private Long likeNum;

    /** 创建时间（MP 自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间（MP 自动填充） */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
