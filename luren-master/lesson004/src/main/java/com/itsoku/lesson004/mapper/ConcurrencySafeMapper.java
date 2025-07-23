package com.itsoku.lesson004.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsoku.lesson004.po.ConcurrencySafePO;

public interface ConcurrencySafeMapper extends BaseMapper<ConcurrencySafePO> {

    /**
     * 乐观锁更新 ConcurrencySafePO
     *
     * @param po
     * @return
     */
    int optimisticUpdate(ConcurrencySafePO po);
}
