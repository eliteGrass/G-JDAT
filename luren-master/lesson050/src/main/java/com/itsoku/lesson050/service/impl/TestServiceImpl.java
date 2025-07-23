package com.itsoku.lesson050.service.impl;

import com.itsoku.lesson050.mapper.TestMapper;
import com.itsoku.lesson050.po.TestPO;
import com.itsoku.lesson050.service.ITestService;
import com.itsoku.lesson050.utils.MoreThreadTransactionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/14 19:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class TestServiceImpl implements ITestService {
    @Autowired
    private TestMapper testMapper;
    public static List<TestPO> data = data();

    private static List<TestPO> data() {
        int userSize = 100000;
        List<TestPO> testPOList = new ArrayList<>(userSize);
        for (long i = 1; i <= userSize; i++) {
            testPOList.add(TestPO.builder().id(i).name("用户-" + i).build());
        }
        return testPOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void singleThreadInsert() {
        // 分别向 5 张表各插入 10 万条数据
        this.testMapper.batchInsert("t_test_1", data);
        this.testMapper.batchInsert("t_test_2", data);
        this.testMapper.batchInsert("t_test_3", data);
        this.testMapper.batchInsert("t_test_4", data);
        this.testMapper.batchInsert("t_test_5", data);
    }

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public void moreThreadInsert() {
        // 事务多线程事务，向 5 张表插入 20 万条数据
        boolean success = MoreThreadTransactionUtils.execute(this.platformTransactionManager,
                () -> this.testMapper.batchInsert("t_test_1", data),
                () -> this.testMapper.batchInsert("t_test_2", data),
                () -> this.testMapper.batchInsert("t_test_3", data),
                () -> this.testMapper.batchInsert("t_test_4", data),
                () -> this.testMapper.batchInsert("t_test_5", data));
        // 所有的插入是否都成功了？
        if (success) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    @Override
    public void moreThreadInsertFail() {
        // 事务多线程事务，向 5 张表插入 20 万条数据，插入第5表的时候，会手动丢个异常，让多线程事务都回滚
        boolean success = MoreThreadTransactionUtils.execute(this.platformTransactionManager,
                () -> this.testMapper.batchInsert("t_test_1", data),
                () -> this.testMapper.batchInsert("t_test_2", data),
                () -> this.testMapper.batchInsert("t_test_3", data),
                () -> this.testMapper.batchInsert("t_test_4", data),
                () -> {
                    this.testMapper.batchInsert("t_test_5", data);
                    throw new RuntimeException("手动抛出异常，模拟失败场景");
                });
        // 所有的插入是否都成功了？
        if (success) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }

    }

    @Override
    public void delete() {
        for (int i = 1; i <= 5; i++) {
            String tableName = "t_test_" + i;
            this.testMapper.delete(tableName);
        }
    }
}
