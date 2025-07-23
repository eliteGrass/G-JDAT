package com.itsoku.lesson025.service;

import com.github.pagehelper.PageHelper;
import com.itsoku.lesson025.dto.UserPageQuery;
import com.itsoku.lesson025.mapper.UserMapper;
import com.itsoku.lesson025.page.PageQuery;
import com.itsoku.lesson025.page.PageResult;
import com.itsoku.lesson025.po.UserPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页获取用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    public PageResult<UserPO> selectPage(int pageNum, int pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize, true);
        try {
            List<UserPO> users = this.userMapper.selectPage();
            return PageResult.of(users);
        } finally {
            //清理分页信息
            PageHelper.clearPage();
        }
    }

    /**
     * 分页获取用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    public PageResult<UserPO> selectPageNew(int pageNum, int pageSize) {
        PageQuery pageQuery = PageQuery.of(pageNum, pageSize, true);
        List<UserPO> users = this.userMapper.selectPageNew(pageQuery);
        return PageResult.of(users);
    }

    /**
     * 分页获取用户信息
     *
     * @param query
     * @return
     */
    public PageResult<UserPO> userPage(UserPageQuery query) {
        if (StringUtils.isNotBlank(query.getKeyword())) {
            query.setKeyword("%" + query.getKeyword() + "%");
        }
        List<UserPO> users = this.userMapper.userPage(query);
        return PageResult.of(users);
    }
}
