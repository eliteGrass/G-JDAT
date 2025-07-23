package com.itsoku.lesson030.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson030.common.BusinessExceptionUtils;
import com.itsoku.lesson030.dto.UserRegisterRequest;
import com.itsoku.lesson030.mapper.UserMapper;
import com.itsoku.lesson030.mq.sender.IMsgSender;
import com.itsoku.lesson030.po.UserPO;
import com.itsoku.lesson030.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {

    @Autowired
    private IMsgSender msgSender;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(UserRegisterRequest req) {
        UserPO userPO = new UserPO();
        userPO.setId(IdUtil.fastSimpleUUID());
        userPO.setName(req.getName());
        this.save(userPO);

        //发送用户注册消息
        this.msgSender.send(userPO);

        return userPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerError(UserRegisterRequest req) {
        UserPO userPO = new UserPO();
        userPO.setId(IdUtil.fastSimpleUUID());
        userPO.setName(req.getName());
        this.save(userPO);

        //发送用户注册消息
        this.msgSender.send(userPO);

        throw BusinessExceptionUtils.businessException("故意失败，看消息投递情况");
    }
}
