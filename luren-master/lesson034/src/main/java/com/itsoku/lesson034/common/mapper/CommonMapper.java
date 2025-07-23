package com.itsoku.lesson034.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itsoku.lesson034.utils.CollUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 11:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface CommonMapper<T> extends BaseMapper<T> {
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        if (CollUtils.isEmpty(idList)) {
            return CollUtils.emptyArrayList();
        }
        return this.selectBatchIds(idList);
    }

    default T findOne(Wrapper<T> queryWrapper) {
        Page<T> page = this.selectPage(new Page<>(1, 1, false), queryWrapper);
        return CollUtils.getFirst(page.getRecords());
    }
}
