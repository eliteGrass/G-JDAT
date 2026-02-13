package com.liteGrass.designPatterns.structuralType.proxyMode.summary.service;


import com.liteGrass.designPatterns.structuralType.proxyMode.summary.util.ConnectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: UserServiceProxy
 * @Author: liteGrass
 * @Date: 2025/11/4 10:44
 * @Description: 代理对象
 */
@Slf4j
public class UserServiceProxy implements UserService {

    private UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void save() {
        ConnectionUtils.Connection conn = ConnectionUtils.getConnection();
        conn.beginTransaction();
        userService.save();
        conn.commit();
    }

}
