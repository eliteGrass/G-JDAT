package com.itsoku.utils;

import com.itsoku.common.BusinessException;
import com.itsoku.common.ResultDto;
import com.itsoku.common.ResultException;

import java.util.Map;

/**
 * <b>description</b>：结果工具类 <br>
 * <b>time</b>：2018-07-26 18:27 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public class ResultUtils {
    public static final String SUCCESS_CODE = "1";
    public static final String SUCCESS_MSG = "操作成功!";
    public static final String ERROR_CODE = "0";
    public static final String ERROR_MSG = "操作失败";

    /**
     * 判断结果是否成功
     *
     * @param resultDto
     * @return
     */
    public static boolean isSuccess(ResultDto resultDto) {
        return resultDto != null && SUCCESS_CODE.equals(resultDto.getState());
    }

    /**
     * 判断结果是否是失败的
     *
     * @param resultDto
     * @return
     */
    public static boolean isError(ResultDto resultDto) {
        return !isSuccess(resultDto);
    }

    /**
     * 构建结果
     *
     * @param code    编码
     * @param subCode 子编码
     * @param msg     提示信息
     * @param data    数据
     * @param extData 扩展数据
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> build(String code, String subCode, String msg, T data, Map extData) {
        return ResultDto.<T>builder().state(code).subCode(subCode).msg(msg).data(data).extData(extData).time(FrameUtils.getTime()).build();
    }

    /**
     * 成功结果
     *
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> success() {
        return successMsg(SUCCESS_MSG);
    }

    /**
     * 成功结果
     *
     * @param msg 提示信息
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> successMsg(String msg) {
        return build(SUCCESS_CODE, null, msg, null, null);
    }

    /**
     * 成功结果
     *
     * @param data 数据
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> successData(T data) {
        return successMsg(SUCCESS_MSG, data);
    }

    /**
     * 成功结果
     *
     * @param msg  提示信息
     * @param data 数据
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> successMsg(String msg, T data) {
        return build(SUCCESS_CODE, null, msg, data, null);
    }

    /**
     * 失败结果
     *
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> error() {
        return error(ERROR_MSG);
    }

    /**
     * 失败结果
     *
     * @param msg 提示信息
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> error(String msg) {
        return build(ERROR_CODE, null, msg, null, null);
    }

    /**
     * 失败结果
     *
     * @param msg 提示信息
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> errorSubCode(String subCode, String msg) {
        return build(ERROR_CODE, subCode, msg, null, null);
    }

    /**
     * 将BaseException转换为ResultDto
     *
     * @param e
     * @param <T>
     * @return
     * @see BusinessException
     */
    public static <T> ResultDto<T> resultDto(BusinessException e) {
        if (e != null) {
            return build(e.getCode(), e.getSubCode(), e.getMsg(), null, e.getExtData());
        }
        return null;
    }

    /**
     * 是否是成功的返回结果，否则抛出异常
     *
     * @param resultDto
     * @param <T>
     * @return
     */
    public static <T> ResultDto<T> isOK(ResultDto<T> resultDto) {
        if (isError(resultDto)) {
            throw new ResultException(resultDto);
        }
        return resultDto;
    }
}
