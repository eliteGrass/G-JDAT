package com.itsoku.lesson024.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson024.dto.UserAddRequest;
import com.itsoku.lesson024.dto.UserModifyRequest;
import com.itsoku.lesson024.po.UserPO;

/**
 * <b>description</b>：用户服务 <br>
 * <b>time</b>： 13:38 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IUserService extends IService<UserPO> {
    /**
     * 新增用户
     *
     * @param req
     * @return 用户id
     */
    String add(UserAddRequest req);

    /**
     * 修改用户
     *
     * @param req
     * @return
     */
    boolean modify(UserModifyRequest req);

    /**
     * 删除用户信息
     *
     * @param userId
     */
    boolean delete(String userId);
}
