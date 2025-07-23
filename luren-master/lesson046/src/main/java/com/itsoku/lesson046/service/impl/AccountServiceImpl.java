package com.itsoku.lesson046.service.impl;

import com.itsoku.lesson046.mapper.AccountMapper;
import com.itsoku.lesson046.mapper.UserMapper;
import com.itsoku.lesson046.po.AccountPO;
import com.itsoku.lesson046.po.UserPO;
import com.itsoku.lesson046.service.IAccountService;
import com.itsoku.lesson046.service.IUserService;
import com.itsoku.orm.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 14:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class AccountServiceImpl extends ServiceImpl<String, AccountMapper, AccountPO> implements IAccountService {
}
