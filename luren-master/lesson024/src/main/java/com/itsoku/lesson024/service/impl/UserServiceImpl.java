package com.itsoku.lesson024.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson024.dto.UserAddRequest;
import com.itsoku.lesson024.dto.UserModifyRequest;
import com.itsoku.lesson024.mapper.UserMapper;
import com.itsoku.lesson024.po.UserPO;
import com.itsoku.lesson024.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UserAddRequest req) {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(req, userPO);
        userPO.setId(IdUtil.fastSimpleUUID());

        this.save(userPO);
        return userPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modify(UserModifyRequest req) {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(req, userPO);

        return this.updateById(userPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String userId) {
        return this.removeById(userId);
    }
}
