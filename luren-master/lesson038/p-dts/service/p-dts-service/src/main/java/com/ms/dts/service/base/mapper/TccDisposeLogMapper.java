package com.ms.dts.service.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ms.dts.base.model.TccDisposeLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>description</b>：tcc分布式事务->补偿日志mapper <br>
 * <b>time</b>：2019-04-13 11:21:49 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Mapper
public interface TccDisposeLogMapper extends BaseMapper<TccDisposeLogPO> {
}