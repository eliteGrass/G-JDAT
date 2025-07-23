package com.itsoku.lesson067.controller;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson067.common.Result;
import com.itsoku.lesson067.common.ResultUtils;
import com.itsoku.lesson067.dto.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/22 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class AccountController {
    @RequestMapping("/account/transfer")
    public Result<String> transfer(@RequestBody TransferRequest request) {
        log.info("转账成功：{}", JSONUtil.toJsonStr(request));
        return ResultUtils.success("转账成功");
    }
}
