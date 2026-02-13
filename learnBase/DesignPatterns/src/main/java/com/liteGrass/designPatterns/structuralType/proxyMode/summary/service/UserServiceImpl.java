package com.liteGrass.designPatterns.structuralType.proxyMode.summary.service;


import com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: UserServiceImpl
 * @Author: liteGrass
 * @Date: 2025/11/4 10:45
 * @Description:
 */
@Slf4j
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void save() {
        log.info("UserServiceImpl --- save");
        userMapper.save();
    }

}
