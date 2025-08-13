package com.liteGrass.learnFlow.event;


import cn.hutool.core.thread.ThreadUtil;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName: StartEventDemo
 * @Author: liteGrass
 * @Date: 2025/8/4 19:41
 * @Description: 开始时间
 */
public class StartEventDemo {

    private static final Logger log = LoggerFactory.getLogger(StartEventDemo.class);

    /**
    * @Auther: liteGrass
    * @Date: 2025/8/4 19:42
    * @Desc: 定时器开始事件
    */
    @Test
    void testMethodTimeEvent() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .key("timeStartEvent")
                .name("定时器开始事件流程")
                .category("startEvent")
                .tenantId("demo")
                .addClasspathResource("processes/learnTimeStartEvent.bpmn20.xml")
                .deploy();

        // 定时器流程在开始的时候不用手动启动，它会自动进行启动，我们手动启动不会影响定时器自己的启动，使用定时器需要手动开启配置asyncExecutorActivate

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("timeStartEvent")
                .latestVersion()
                .singleResult();

        ThreadUtil.sleep(1000 * 90);

        // 获取该流程定义运行的实例，并进行相关任务的查询
        RuntimeService runtimeService = processEngine.getRuntimeService();
        long processInstanceCount = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("timeStartEvent")
                .count();
        log.info("运行的流程实例个数为：{}", processInstanceCount);


        TaskService taskService = processEngine.getTaskService();
        List<Task> taskCount = taskService.createTaskQuery()
                .processDefinitionKey("timeStartEvent")
                .list();
        log.info("当前流程定义的任务个数为:{}", taskCount.size());

    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/4 21:06
    * @Desc: 信号开始事件
    */
    @Test
    void testMethodSignStartEvent() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // sid-22563c47-0333-4672-83c5-7030f27f6c62
        // sid-53f9939c-8cb3-410d-8ea2-f0dba00ea366
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/learnSignStartEvent.bpmn20.xml")
                .key("signStartEvent")
                .name("信号开始事件")
                .tenantId("demo")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("learnSignStartEvent")
                .latestVersion()
                .singleResult();

        System.out.println(processDefinition);

        // 发布信号
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.signalEventReceived("sid-53f9939c-8cb3-410d-8ea2-f0dba00ea366");
        runtimeService.signalEventReceivedWithTenantId("sid-53f9939c-8cb3-410d-8ea2-f0dba00ea366", "demo");

        System.out.println();

    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/4 22:01
    * @Desc: 消息开始事件
    */
    @Test
    void testMethodMessageStartEvent() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        

    }

}
