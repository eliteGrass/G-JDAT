package com.ms.dts.tcc.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.itsoku.common.BusinessException;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.itsoku.utils.SignUtils;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.tcc.branch.ExceptionDto;

import java.util.Map;
import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 9:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class TccUtils {

    /**
     * 获取异常中的BaseException信息
     *
     * @param e
     * @return
     */
    public static ExceptionDto getExceptionDto(Throwable e) {
        if (e == null) {
            return null;
        }
        BusinessException baseException = getBaseException(e);
        String throwableStack = ExceptionUtil.stacktraceToString(e);
        if (Objects.nonNull(baseException)) {
            return new ExceptionDto(baseException.getCode(), baseException.getSubCode(), baseException.getMsg(), baseException.getExtData(), throwableStack);
        }
        return new ExceptionDto(ResultUtils.ERROR_CODE, null, "系统繁忙,请稍后再试!", null, throwableStack);
    }

    public static BusinessException getBaseException(Throwable e) {
        if (e == null) {
            return null;
        } else if (e instanceof BusinessException) {
            return (BusinessException) e;
        } else {
            return getBaseException(e.getCause());
        }
    }

    /**
     * 对tccRecordModel签名
     *
     * @param tccRecordPO
     * @return
     */
    public static String sign(TccRecordPO tccRecordPO) {
        Map<String, Object> paramMap = FrameUtils.newHashMap("id", tccRecordPO.getId(),
                "addTime", tccRecordPO.getAddtime().toString(),
                "request_data", tccRecordPO.getRequestData());
        return SignUtils.createSign(paramMap, "4b4dae37-8da7-4ce5-b9f6-746b79f343fe");
    }

    /**
     * 签名是否正确
     * 正确 返回true
     * 错误 返回false
     *
     * @param tccRecordPO
     * @param sign
     * @return
     */
    public static boolean isValidSign(TccRecordPO tccRecordPO, String sign) {
        return Objects.nonNull(tccRecordPO) && sign(tccRecordPO).equals(sign);
    }

    public static void assertSuccess(TccRecordPO tccRecordPO, String errorMsg) {
        // 状态，0：待处理，1：处理成功，2：处理失败
        if (tccRecordPO != null && tccRecordPO.getStatus() != null && tccRecordPO.getStatus().intValue() == 1) {
            return;
        }

        FrameUtils.throwBaseException(errorMsg);
    }
}
