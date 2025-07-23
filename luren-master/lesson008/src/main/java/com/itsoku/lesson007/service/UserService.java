package com.itsoku.lesson007.service;

import cn.hutool.core.collection.CollectionUtil;
import com.itsoku.lesson007.dto.User;
import com.itsoku.lesson007.dto.UserExportRequest;
import com.itsoku.lesson007.excel.ExcelExportResponse;
import com.itsoku.lesson007.excel.ExcelExportUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 14:10 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserService {
    public List<User> list = new ArrayList<>();

    public UserService() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setUserId(i);
            user.setUserName("用户名-" + i);
            user.setAge(20 + i);
            user.setAddress("地址-" + i);
            list.add(user);
        }
    }

    public List<User> getUserList() {
        return list;
    }

    /**
     * 根据用户id列表查找用户列表
     *
     * @param userIdList
     * @return
     */
    public List<User> getUserList(List<Integer> userIdList) {
        return this.getUserList().stream().filter(item -> userIdList.contains(item.getUserId())).collect(Collectors.toList());
    }

    /**
     * 导出用户数据
     *
     * @param request
     * @return
     */
    public ExcelExportResponse userExport(UserExportRequest request) {
        List<Integer> userIdList = request.getUserIdList();
        //根据用户id列表获取用户列表
        List<User> userList;
        if (CollectionUtil.isEmpty(userIdList)) {
            userList = this.getUserList();
        } else {
            userList = this.getUserList(request.getUserIdList());
        }
        return ExcelExportUtils.build(userList, request);
    }

}
