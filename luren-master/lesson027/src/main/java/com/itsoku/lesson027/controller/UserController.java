package com.itsoku.lesson027.controller;

import com.itsoku.lesson027.common.Result;
import com.itsoku.lesson027.common.ResultUtils;
import com.itsoku.lesson027.ds.ReadWriteRoutingStrategyHolder;
import com.itsoku.lesson027.po.UserPO;
import com.itsoku.lesson027.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public Result<Map<String, UserPO>> test() {
        //没有路由策略的情况
        UserPO user = this.userService.getUser();
        //走主库获取用户信息
        UserPO userFromMaster = this.userService.getUserFromMaster();
        //走从库获取用户信息
        UserPO userFromSlave = this.userService.getUserFromSlave();
        //强制走主库
        UserPO userHitMaster = ReadWriteRoutingStrategyHolder.hitMaster(() -> this.userService.getUserFromSlave());

        //组装结果返回
        Map<String, UserPO> result = new LinkedHashMap<>();
        result.put("user", user);
        result.put("userFromMaster", userFromMaster);
        result.put("userFromSlave", userFromSlave);
        result.put("userHitMaster", userHitMaster);

        return ResultUtils.success(result);
    }

}
