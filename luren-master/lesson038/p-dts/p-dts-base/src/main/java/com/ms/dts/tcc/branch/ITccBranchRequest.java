package com.ms.dts.tcc.branch;

import java.io.Serializable;

/**
 * <b>description</b>：tcc事务处理参数，请求的所有阶段的信息封装在该对象内 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBranchRequest extends Serializable {
    /**
     * 获取分布式事务id
     *
     * @return
     * @throws Exception
     */
    String getTid();

    /**
     * 设置分布式事务id
     *
     * @param tid
     */
    void setTid(String tid);

    /**
     * 订单类型
     *
     * @return
     * @throws Exception
     */
    Integer getBusType();

    /**
     * 设置订单类型
     *
     * @param busType
     */
    void setOrderType(Integer busType);

    /**
     * 业务订单id
     *
     * @return
     */
    String getBusId();

    /**
     * 设置订单id
     *
     * @param busId
     */
    void setOrderId(String busId);

}
