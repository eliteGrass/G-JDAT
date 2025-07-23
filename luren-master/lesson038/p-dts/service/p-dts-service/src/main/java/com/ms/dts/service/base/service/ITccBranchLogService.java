package com.ms.dts.service.base.service;

import com.itsoku.common.service.IBaseService;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccDisposeLogPO;

import java.util.List;

/**
 * <b>description</b>：分布式事务->分支日志记录业务接口 <br>
 * <b>time</b>：2019-01-23 13:44:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBranchLogService extends IBaseService<TccBranchLogPO> {
    /**
     * 删除数据
     *
     * @param tccRecordId
     * @return
     * @throws Exception
     */
    int deleteByRecordId(String tccRecordId);

    /**
     * 删除tccRecordIdList关联的日志记录
     *
     * @param tccRecordIdList
     * @return
     * @throws Exception
     */
    int deleteByTccRecordIdList(List<Long> tccRecordIdList);

    /**
     * 获取分支执行日志
     *
     * @param tccRecordId
     * @return
     */
    List<TccBranchLogPO> getListByTccRecordId(String tccRecordId);
}