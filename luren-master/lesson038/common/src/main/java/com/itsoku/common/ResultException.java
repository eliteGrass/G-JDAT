package com.itsoku.common;

/**
 * <b>description</b>：结果异常 <br>
 * <b>time</b>：2018-08-02 10:30 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public class ResultException extends RuntimeException {
    /**
     * 结果
     */
    private ResultDto resultDto;

    public ResultDto getResultDto() {
        return resultDto;
    }

    public void setResultDto(ResultDto resultDto) {
        this.resultDto = resultDto;
    }

    public ResultException() {
    }

    public ResultException(ResultDto resultDto) {
        super(resultDto.getMsg());
        this.resultDto = resultDto;
    }
}
