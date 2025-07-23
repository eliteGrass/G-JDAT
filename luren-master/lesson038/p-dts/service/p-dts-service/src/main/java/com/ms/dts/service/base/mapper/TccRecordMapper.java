package com.ms.dts.service.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ms.dts.base.model.TccRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>description</b>：分布式事务记录mapper <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Mapper
public interface TccRecordMapper extends BaseMapper<TccRecordPO> {
}