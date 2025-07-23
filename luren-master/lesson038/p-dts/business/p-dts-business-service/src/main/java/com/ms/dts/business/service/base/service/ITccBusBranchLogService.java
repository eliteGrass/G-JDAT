package com.ms.dts.business.service.base.service;

import com.itsoku.common.service.IBaseService;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;

import java.util.List;

/**
 * <b>description</b>：业务库中分支事务日志记录业务接口 <br>
 * <b>time</b>：2019-01-24 16:43:51 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBusBranchLogService extends IBaseService<TccBusBranchLogPO> {
    /**
     * 获取对象
     *
     * @param tccRecordId
     * @param classname
     * @param method      method的值见{@link TccBranchMethodEnums}
     * @return
     * @throws Exception
     */
    TccBusBranchLogPO get(String tccRecordId, String classname, int method);

    /**
     * 删除tccRecordIdList关联的日志记录
     *
     * @param tccRecordIdList
     * @return
     * @throws Exception
     */
    int deleteByTccRecordIdList(List<String> tccRecordIdList);
}