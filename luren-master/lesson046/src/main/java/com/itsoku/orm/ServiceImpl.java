package com.itsoku.orm;

import com.itsoku.lambda.SFunction;
import com.itsoku.orm.page.PageQuery;
import com.itsoku.orm.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 14:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ServiceImpl<Id, M extends BaseMapper<Id, T>, T> implements IService<Id, T> {
    @Autowired
    protected M baseMapper;

    @Override
    public int insert(T po) {
        return baseMapper.insert(po);
    }

    @Override
    public int insertBatch(List<T> poList) {
        return baseMapper.insertBatch(poList);
    }

    @Override
    public int update(T po) {
        return baseMapper.update(po);
    }

    @Override
    public int updateNonNull(T po) {
        return baseMapper.updateNonNull(po);
    }

    @Override
    public int updateWith(T po, SFunction<T, ?>... props) {
        return baseMapper.updateWith(po, props);
    }

    @Override
    public int optimisticUpdate(T po) {
        return baseMapper.optimisticUpdate(po);
    }

    @Override
    public int optimisticUpdateNonNull(T po) {
        return baseMapper.optimisticUpdateNonNull(po);
    }

    @Override
    public int optimisticUpdateWith(T po, SFunction<T, ?>... props) {
        return baseMapper.optimisticUpdateWith(po, props);
    }

    @Override
    public int deleteById(Id id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<Id> idList) {
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public int delete(Criteria<T> criteria) {
        return baseMapper.delete(criteria);
    }

    @Override
    public List<T> find(Criteria<T> criteria) {
        return baseMapper.find(criteria);
    }

    @Override
    public T findOne(Criteria<T> criteria) {
        return baseMapper.findOne(criteria);
    }

    @Override
    public T findById(Id id) {
        return baseMapper.findById(id);
    }

    @Override
    public List<T> findByIds(Collection<Id> ids) {
        return baseMapper.findByIds(ids);
    }

    @Override
    public Map<Id, T> findMapByIds(Collection<Id> ids) {
        return baseMapper.findMapByIds(ids);
    }

    @Override
    public long count(Criteria<T> criteria) {
        return baseMapper.count(criteria);
    }

    @Override
    public PageResult<T> findPage(PageQuery pageQuery, Criteria<T> criteria) {
        return baseMapper.findPage(pageQuery, criteria);
    }
}
