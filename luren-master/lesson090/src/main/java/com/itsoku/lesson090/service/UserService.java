package com.itsoku.lesson090.service;

import com.itsoku.lesson090.mapper.UserMapper;
import com.itsoku.lesson090.po.UserPO;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 22:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void m1() {
        this.m2("1", "m1");
    }

    @Transactional
    public void m2(String id, String name) {
        //向用户表插入一条记录
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setName(name);
        this.userMapper.insert(userPO);

        //抛出异常，看看事务是否会回滚
        throw new RuntimeException("故意失败，看下事务是否会回滚");
    }

    //注入自己，userService就是被AOP包装后的代理对象
    @Autowired
    private UserService userService;

    public void m3() {
        //通过userService访问m2，事务会生效
        this.userService.m2("3", "m3");
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void m4() {
        this.transactionTemplate.executeWithoutResult(action -> {
            this.m2("4", "m4");
        });
    }

    public void m5() {
        //通过Spring提供的工具类AopContext.currentProxy()，可以获取当前代理对象，将类型强制转换为UserService，然后调用m2方法，事务也会生效
        UserService as = (UserService) AopContext.currentProxy();
        as.m2("5", "m5");
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void m6() throws Exception {
        //向用户表插入一条记录
        UserPO userPO = new UserPO();
        userPO.setId("6");
        userPO.setName("m6");
        this.userMapper.insert(userPO);

        //抛出一个Exception异常，看看事务是否会回滚
        throw new Exception("抛出Exception异常");
    }

    @Transactional(rollbackFor = Exception.class)
    public void m7() throws Exception {
        try {
            //向用户表插入一条记录
            UserPO userPO = new UserPO();
            userPO.setId("7");
            userPO.setName("m7");
            this.userMapper.insert(userPO);

            //抛出一个Exception异常，
            throw new RuntimeException("抛出Exception异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
