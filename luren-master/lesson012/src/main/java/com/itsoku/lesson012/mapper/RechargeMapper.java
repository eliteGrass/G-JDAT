package com.itsoku.lesson012.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson012.po.RechargePO;
import org.apache.ibatis.annotations.Param;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface RechargeMapper extends BaseMapper<RechargePO> {
    /**
     * 将充值记录状态更新为成功（将status作为条件判断的方式）
     *
     * @param rechargeId
     * @return
     */
    int updateRechargeSuccess(@Param("rechargeId") String rechargeId);

    /**
     * 将充值记录状态更新为成功（乐观锁的方式）
     *
     * @param rechargeId
     * @param expectVersion 期望版本号
     * @return
     */
    int updateRechargeSuccessOptimisticLock(@Param("rechargeId") String rechargeId, @Param("expectVersion") Long expectVersion);
}
