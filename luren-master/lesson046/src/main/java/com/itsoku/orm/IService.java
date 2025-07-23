package com.itsoku.orm;

import com.itsoku.lambda.SFunction;
import com.itsoku.orm.page.PageQuery;
import com.itsoku.orm.page.PageResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 14:10 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IService<Id, T> {
    /**
     * 插入记录
     *
     * @param po
     * @return 影响行数
     */
    int insert(T po);

    /**
     * 批量插入
     *
     * @param poList
     * @return 影响行数
     */
    int insertBatch(List<T> poList);

    /**
     * 按照主键更新一个对象
     *
     * @param po
     * @return 影响行数
     */
    int update(T po);

    /**
     * 按照主键更新一个对象，只更新非 null 字段
     *
     * @param po
     * @return 影响行数
     */
    int updateNonNull(T po);

    /**
     * 按照主键更新一个对象，只更新指定的属性
     *
     * @param po
     * @param props 需更新的属性列表
     * @return 影响行数
     */
    int updateWith(T po, SFunction<T, ?>... props);

    /**
     * 按照主键和version字段作为条件更新
     *
     * @param po
     * @return 影响行数
     */
    int optimisticUpdate(T po);

    /**
     * 按照主键和version字段作为条件更新，只更新非 null 字段
     *
     * @param po
     * @return 影响行数
     */
    int optimisticUpdateNonNull(T po);

    /**
     * 按照主键和version字段作为条件更新，只更新指定的属性
     *
     * @param po
     * @param props 需更新的属性列表
     * @return 影响行数
     */
    int optimisticUpdateWith(T po, SFunction<T, ?>... props);

    /**
     * 根据主键删除记录
     *
     * @param id
     * @return 影响行数
     */
    int deleteById(Id id);

    /**
     * 根据主键批量删除记录
     *
     * @param idList
     * @return 影响行数
     */
    int deleteByIds(Collection<Id> idList);

    /**
     * 根据条件删除记录
     *
     * @param criteria
     * @return 影响行数
     */
    int delete(Criteria<T> criteria);

    /**
     * 根据条件查询记录
     *
     * @param criteria
     * @return
     */
    List<T> find(Criteria<T> criteria);

    /**
     * 分页查询
     *
     * @param criteria
     * @return
     */
    T findOne(Criteria<T> criteria);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    T findById(Id id);

    /**
     * 根据主键列表查询多条列表
     *
     * @param idList
     * @return
     */
    List<T> findByIds(Collection<Id> idList);

    /**
     * 根据主键列表查询记录，返回Map，key为主键，map为记录
     *
     * @param idList
     * @return
     */
    Map<Id, T> findMapByIds(Collection<Id> idList);

    /**
     * 查询满足条件的记录数
     *
     * @param criteria
     * @return
     */
    long count(Criteria<T> criteria);

    /**
     * 按条件分页查询
     *
     * @param pageQuery 分页条件
     * @param criteria 查询条件
     * @return
     */
    PageResult<T> findPage(PageQuery pageQuery, Criteria<T> criteria);
}
