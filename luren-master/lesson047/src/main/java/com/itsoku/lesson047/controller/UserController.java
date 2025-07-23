package com.itsoku.lesson047.controller;

import com.itsoku.lesson047.po.UserPO;
import com.itsoku.lesson047.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 获取当前租户所有用户列表
     *
     * @return
     */
    @GetMapping("/user/list")
    public List<UserPO> list() {
        return this.userService.list();
    }

    /**
     * 创建用户
     *
     * @param userName
     * @return
     */
    @PostMapping("/user/insert")
    public UserPO insert(@RequestParam("userName") String userName) {
        UserPO userPO = new UserPO();
        userPO.setUserName(userName);
        this.userService.save(userPO);
        return this.userService.getById(userPO.getId());
    }

}
