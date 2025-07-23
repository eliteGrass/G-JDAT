package com.itsoku.lesson007.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson007.dto.UserExportRequest;
import com.itsoku.lesson007.excel.ExcelExportResponse;
import com.itsoku.lesson007.excel.ExcelExportUtils;
import com.itsoku.lesson007.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:14 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Controller
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/userList")
    public String userList(Model model) {
        model.addAttribute("userList", this.userService.getUserList());
        return "userList";
    }

    @PostMapping("/userExport")
    @ResponseBody
    public ExcelExportResponse userExport(@RequestBody UserExportRequest userExportRequest) throws IOException {
        LOGGER.info("userExportRequest:{}", JSONUtil.toJsonPrettyStr(userExportRequest));
        return this.userService.userExport(userExportRequest);
    }

}
