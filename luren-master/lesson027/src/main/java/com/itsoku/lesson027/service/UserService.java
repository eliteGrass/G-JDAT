package com.itsoku.lesson027.service;

import com.itsoku.lesson027.ds.ReadWrite;
import com.itsoku.lesson027.ds.ReadWriteRoutingStrategy;
import com.itsoku.lesson027.mapper.UserMapper;
import com.itsoku.lesson027.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserPO getUser() {
        return this.userMapper.getUser(1);
    }

    /**
     * 从主库获取用户
     *
     * @return
     */
    @ReadWrite(ReadWriteRoutingStrategy.MASTER)
    public UserPO getUserFromMaster() {
        return this.userMapper.getUser(1);
    }

    /**
     * 从库获取用户
     *
     * @return
     */
    @ReadWrite(ReadWriteRoutingStrategy.SLAVE)
    public UserPO getUserFromSlave() {
        return this.userMapper.getUser(1);
    }
}
