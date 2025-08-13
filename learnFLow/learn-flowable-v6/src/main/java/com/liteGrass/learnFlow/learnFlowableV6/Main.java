package com.liteGrass.learnFlow.learnFlowableV6;

import org.flowable.engine.*;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 他会创建spring的ProcessEngineConfiguration， 然后反射获取buildProcessEngine方法进行创建
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        // 流程存储服务
        RepositoryService repositoryService = engine.getRepositoryService();
        //部署流程
        repositoryService.createDeployment()
                .addClasspathResource("processes/LeaveApplyProcess.bpmn20.xml")
                .deploy();

        // 流程运行时服务
        RuntimeService runtimeService = engine.getRuntimeService();
        //启动流程任务
        runtimeService.startProcessInstanceByKey("LeaveApplyProcess");


        // 流程任务服务
        TaskService taskService = engine.getTaskService();
        // 查询流程任务
        TaskQuery taskQuery = taskService.createTaskQuery();
        log.info("当前流程任务总数为:{}", taskQuery.count());
        Task firstTask = taskQuery.taskAssignee("liuxiaopeng").singleResult();
        taskService.complete(firstTask.getId());
        log.info("用户任务：{}办理完成，办理人为：{}", firstTask.getName(), firstTask.getAssignee());

        // 查询第二个流程任务
        Task secondTask = taskQuery.taskAssignee("hebo").singleResult();
        taskService.complete(secondTask.getId());
        log.info("用户任务：{}办理完成，办理人为：{}", secondTask.getName(), secondTask.getAssignee());
        log.info("当前流程任务总数为:{}", taskQuery.count());

        engine.close();
    }
}