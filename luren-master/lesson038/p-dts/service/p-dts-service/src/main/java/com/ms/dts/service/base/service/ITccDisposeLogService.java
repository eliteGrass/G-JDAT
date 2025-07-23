package com.ms.dts.service.base.service;

import com.itsoku.common.service.IBaseService;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccDisposeLogPO;

import java.util.List;

/**
 * <b>description</b>：tcc分布式事务->补偿日志业务接口 <br>
 * <b>time</b>：2019-04-13 11:21:49 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccDisposeLogService extends IBaseService<TccDisposeLogPO> {
    /**
     * 删除tccRecordIdList关联的日志记录
     *
     * @param tccRecordIdList
     * @return
     * @throws Exception
     */
    int deleteByTccRecordIdList(List<Long> tccRecordIdList);

    /**
     * 获取补偿日志
     *
     * @param tccRecordId
     * @return
     */
    List<TccDisposeLogPO> getListByTccRecordId(String tccRecordId);
}