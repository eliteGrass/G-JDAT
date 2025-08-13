package com.liteGrass.learnFlow.LearnFlowableV6;


import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.common.engine.api.management.TablePage;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ProcessManagerDemo
 * @Author: liteGrass
 * @Date: 2025/8/1 7:01
 * @Description: 管理相关接口
 */
public class ProcessManagerDemo {

    /**
    * @Auther: liteGrass
    * @Date: 2025/8/1 7:01
    * @Desc: 管理API
    */
    @Test
    void testMethodManager() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        ManagementService managementService = processEngine.getManagementService();

        Map<String, Long> tableCount = managementService.getTableCount();


        String tableName = tableCount.keySet().stream().max((x, y) -> tableCount.get(x).compareTo(tableCount.get(y))).get();
        TableMetaData tableMetaData = managementService.getTableMetaData(tableName);
        List<String> columnNames = tableMetaData.getColumnNames();

        TablePage tablePage = managementService.createTablePageQuery().tableName(tableName).listPage(0, 10);

        System.out.println();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/1 7:31
    * @Desc: 定时任务执行，异步任务进行执行
    */
    @Test
    void testMethodTimeExec() {

    }

}
