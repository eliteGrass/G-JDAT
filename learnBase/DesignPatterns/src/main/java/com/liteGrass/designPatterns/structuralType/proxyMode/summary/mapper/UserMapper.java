package com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper;


/**
 * @InterfaceName: UserMapper
 * @Author: liteGrass
 * @Date: 2025/11/4 16:41
 * @Description: 用户查询
 */
public interface UserMapper {

    @Insert(value = "insert into user")
    void save();
}
