package com.itsoku.lesson025.controller;

import com.itsoku.lesson025.common.Result;
import com.itsoku.lesson025.common.ResultUtils;
import com.itsoku.lesson025.dto.UserPageQuery;
import com.itsoku.lesson025.page.PageResult;
import com.itsoku.lesson025.po.UserPO;
import com.itsoku.lesson025.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用Aop简化MyBatis分页
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/selectPage")
    public Result<PageResult<UserPO>> selectPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        PageResult<UserPO> page = this.userService.selectPage(pageNum, pageSize);
        return ResultUtils.success(page);
    }

    @GetMapping("/selectPageNew")
    public Result<PageResult<UserPO>> selectPageNew(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        PageResult<UserPO> page = this.userService.selectPageNew(pageNum, pageSize);
        return ResultUtils.success(page);
    }

    @GetMapping("/userPage")
    public Result<PageResult<UserPO>> userPage(UserPageQuery query) {
        PageResult<UserPO> page = this.userService.userPage(query);
        return ResultUtils.success(page);
    }
}
