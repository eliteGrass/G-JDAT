package com.itsoku.lesson050.mapper;

import com.itsoku.lesson050.po.TestPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/14 19:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface TestMapper {
    void _batchInsert(@Param("tableName") String tableName, @Param("list") List<TestPO> list);

    default void batchInsert(String tableName, List<TestPO> list) {
//        long st = System.currentTimeMillis();
        int batchSize = 1000;
        int size = list.size();
        int batch = size % batchSize == 0 ? size / batchSize : size / batchSize + 1;
        for (int i = 0; i < batch; i++) {
            int fromIndex = i * batchSize;
            int toIndex = fromIndex + batchSize;
            if (toIndex > size) {
                toIndex = size;
            }
            List<TestPO> insertList = list.subList(fromIndex, toIndex);
            this._batchInsert(tableName, insertList);
        }
//        System.out.println("time:"+(System.currentTimeMillis() - st));
    }


    @Delete("delete from ${tableName}")
    void delete(@Param("tableName") String tableName);
}
