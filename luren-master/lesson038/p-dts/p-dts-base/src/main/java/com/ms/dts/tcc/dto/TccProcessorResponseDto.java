package com.ms.dts.tcc.dto;

import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBranchLogPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <b>description</b>：tcc补偿处理器响应信息 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TccProcessorResponseDto implements Serializable {
    //事务记录id
    private String tccRecordId;
    //tcc事务状态，最终事务是成功还是失败
    private ResultStatus resultStatus;
    //事务是否已结束
    private Boolean finish = Boolean.FALSE;
    //业务分支日志
    private List<TccBranchLogPO> tccBranchLogPOList;
}
