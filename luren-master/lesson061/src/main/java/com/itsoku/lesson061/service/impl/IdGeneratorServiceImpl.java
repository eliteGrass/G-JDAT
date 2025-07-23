package com.itsoku.lesson061.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itsoku.lesson061.mapper.IdGeneratorMapper;
import com.itsoku.lesson061.po.IdGeneratorPO;
import com.itsoku.lesson061.service.IdGeneratorService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/6 13:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Autowired
    private IdGeneratorMapper idGeneratorMapper;

    /**
     * 提前生成多少个id放在内存中以供使用？
     */
    private static final Long STEP_SIZE = 100L;
    private static Map<String, IdInterval> idMap = new HashMap<>();
    private static Object idMapLock = new Object();

    public List<Long> getIdList(String code, int num) {
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            idList.add(getId(code));
        }
        return idList;
    }

    /**
     * 获取id
     *
     * @param code
     * @return
     * @throws Exception
     */
    public Long getId(String code) {
        IdInterval idInterval = idMap.get(code);
        if (idInterval == null) {
            synchronized (idMapLock) {
                idInterval = idMap.get(code);
                if (idInterval == null) {
                    idInterval = this.getIdInterval(code);
                    idMap.put(code, idInterval);
                }
            }
        }
        long value = idInterval.getCurValue().getAndIncrement();
        if (value > idInterval.getRight()) {
            synchronized (idMapLock) {
                if (idInterval == idMap.get(code)) {
                    idMap.put(code, this.getIdInterval(code));
                }
            }
            value = getId(code);
        }
        return value;
    }

    private IdInterval getIdInterval(String code) {
        //根据code查询记录，不存在，则创建
        IdGeneratorPO idGeneratorPO = getIdGeneratorPOByCode(code);
        //区间左右值
        long left, right;
        if (idGeneratorPO == null) {
            left = 1;
            right = left + STEP_SIZE - 1;
            idGeneratorPO = new IdGeneratorPO();
            idGeneratorPO.setCode(code);
            idGeneratorPO.setMaxId(right);
            this.idGeneratorMapper.insert(idGeneratorPO);
        } else {
            while (true) {
                Long curValue = idGeneratorPO.getMaxId();
                left = curValue + 1;
                right = left + STEP_SIZE - 1;
                /**
                 * 更新max_id：update t_id_generator set max_id = #{right} where code = #{CODE} and max_id = #{curValue}
                 * 这里为了防止并发更新，条件上加上了 max_id ，相当于用了乐观锁，更新的时候根据影响行数判断是否更新成功，不成功重试
                 */
                LambdaUpdateWrapper<IdGeneratorPO> updateWrapper = Wrappers.lambdaUpdate(IdGeneratorPO.class)
                        .eq(IdGeneratorPO::getCode, idGeneratorPO.getCode())
                        .eq(IdGeneratorPO::getMaxId, curValue)
                        .set(IdGeneratorPO::getMaxId, right);
                //update行数==1，表示成功，则退出循环，否则继续重试
                if (this.idGeneratorMapper.update(null, updateWrapper) == 1) {
                    break;
                }
                idGeneratorPO = this.getIdGeneratorPOByCode(code);
            }
        }
        IdInterval idInterval = new IdInterval(left, right);
        return idInterval;
    }

    private IdGeneratorPO getIdGeneratorPOByCode(String code) {
        return this.idGeneratorMapper.selectById(code);
    }

    /**
     * id区间[left,value]，curValue：当前值
     */
    @Data
    private static class IdInterval {
        //区间左值
        private long left;
        //区间右值
        private long right;
        //当前值
        private AtomicLong curValue;

        public IdInterval(long left, long right) {
            this.left = left;
            this.right = right;
            this.curValue = new AtomicLong(this.left);
        }
    }
}
