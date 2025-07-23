package com.itsoku.lesson025.mapper;

import com.itsoku.lesson025.dto.UserPageQuery;
import com.itsoku.lesson025.page.IPageQuery;
import com.itsoku.lesson025.po.UserPO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Mapper
public interface UserMapper {
    @Select("select id,name from t_user_lesson025 order by id asc")
    List<UserPO> selectPage();

    @Select("select id,name from t_user_lesson025 order by id asc")
    List<UserPO> selectPageNew(IPageQuery pageQuery);

    @Select("select id,name from t_user_lesson025 where name like #{query.keyword} order by id asc")
    List<UserPO> userPage(@Param("query") UserPageQuery pageQuery);
}
