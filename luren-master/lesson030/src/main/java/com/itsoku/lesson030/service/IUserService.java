package com.itsoku.lesson030.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson030.dto.UserRegisterRequest;
import com.itsoku.lesson030.po.UserPO;

/**
 * <b>description</b>：用户服务 <br>
 * <b>time</b>： 13:38 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IUserService extends IService<UserPO> {
    String register(UserRegisterRequest req);

    String registerError(UserRegisterRequest req);
}
