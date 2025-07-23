package com.itsoku.lesson037.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson037.utils.CollUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 11:22 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IBaseService<T> extends IService<T> {
    @Override
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        if (CollUtils.isEmpty(idList)) {
            return CollUtils.emptyArrayList();
        }
        return IService.super.listByIds(idList);
    }

    default T findOne(Wrapper<T> queryWrapper) {
        Page<T> page = this.page(new Page<>(1, 1, false), queryWrapper);
        return CollUtils.getFirst(page.getRecords());
    }
}
