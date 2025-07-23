package com.ms.dts.tcc.dto;

import com.ms.dts.base.model.TccRecordPO;
import lombok.*;

import java.io.Serializable;

/**
 * <b>description</b>：tcc补偿处理器请求信息 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TccProcessorRequestDto implements Serializable {
    private TccRecordPO tccRecordPO;
    //是否同步上传结果
    private boolean syncUploadResult;
    //同步还是异步，true: 同步，false：异步（try之后直接返回）
    private boolean sync;
}
