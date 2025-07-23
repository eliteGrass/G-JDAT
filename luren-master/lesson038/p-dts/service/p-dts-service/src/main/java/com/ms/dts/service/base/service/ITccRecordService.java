package com.ms.dts.service.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.common.service.IBaseService;
import com.ms.dts.base.model.TccRecordPO;

/**
 * <b>description</b>：分布式事务记录业务接口 <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccRecordService extends IBaseService<TccRecordPO> {
    /**
     * 获取对象
     *
     * @param busType 订单类型
     * @param busId   订单id
     * @return
     * @throws Exception
     */
    TccRecordPO getModelByOrder(int busType, String busId);

    TccRecordPO insert(TccRecordPO model);
}