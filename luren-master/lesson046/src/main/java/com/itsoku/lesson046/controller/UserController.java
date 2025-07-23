package com.itsoku.lesson046.controller;

import cn.hutool.json.JSONUtil;
import com.itsoku.common.Result;
import com.itsoku.common.ResultUtils;
import com.itsoku.lesson046.mapper.UserMapper;
import com.itsoku.lesson046.po.UserPO;
import com.itsoku.orm.Criteria;
import com.itsoku.orm.page.PageQuery;
import com.itsoku.orm.page.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 10:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/test")
    public Result<Boolean> test() {
        int i = 10 / 0;
        return ResultUtils.success(true);
    }
}
