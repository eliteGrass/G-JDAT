package com.ms.dts.tcc.branch;

import com.itsoku.common.ResultDto;
import com.ms.dts.base.ResultStatus;

/**
 * Tcc分支接口
 * 事务参与者分支需要实现该接口
 * <br /> 若分支不能明确知道处理结果的，返回 {@link ResultStatus#INIT}.
 * 否者返回{@link ResultStatus} 中的其他值
 *
 * @see ResultStatus
 * <b>description</b>：Tcc事务处理接口 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBranch<T extends ITccBranchRequest> {
    String TRY1_METHOD = "try1";
    String CONFIRM_METHOD = "confirm";
    String CANCEL_METHOD = "cancel";

    /**
     * tcc->try方法
     *
     * @param context
     * @return
     */
    ResultDto<TccBranchContext<T>> try1(TccBranchContext<T> context);

    /**
     * tcc->confirm方法
     *
     * @param context
     * @return
     * @throws Exception
     */
    ResultDto<TccBranchContext<T>> confirm(TccBranchContext<T> context);

    /**
     * tcc->confirm方法
     *
     * @param context
     * @return
     * @throws Exception
     */
    ResultDto<TccBranchContext<T>> cancel(TccBranchContext<T> context);
}
