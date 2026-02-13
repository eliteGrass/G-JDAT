package com.liteGrass.bm.likes.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liteGrass.bm.likes.entity.BmLikeCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @InterfaceName: BmLikeMapper
 * @Author: liteGrass
 * @Date: 2025/8/22 23:02
 * @Description: 点赞数
 */
@Mapper
public interface BmLikeMapper extends BaseMapper<BmLikeCount> {
}
