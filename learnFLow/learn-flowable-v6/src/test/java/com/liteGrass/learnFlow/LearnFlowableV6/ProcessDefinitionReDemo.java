package com.liteGrass.learnFlow.LearnFlowableV6;


import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName: RunFirstDemo
 * @Author: liteGrass
 * @Date: 2025/7/29 16:40
 * @Description:
 */
public class ProcessDefinitionReDemo {


    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionReDemo.class);

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/29 16:40
    * @Desc: 测试第一个程序
    */
    @Test
    void testMethodFirstDemo() {
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


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/30 16:41
    * @Desc: 流程定义删除
    */
    @Test
    void testMethodDeleteProcessDefinition() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        // 流程存储服务
        RepositoryService repositoryService = engine.getRepositoryService();

        // 进行部署，查看当前版本号
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/LeaveApplyProcess.bpmn20.xml")
                .deploy();

        // 查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        log.info("流程定义ID为：{}，流程名称为：{}，版本号：{}", processDefinition.getId(), processDefinition.getName(), processDefinition.getVersion());


        // 进行流程定义删除
        repositoryService.deleteDeployment(deploy.getId());
        log.info("删除部署定义为：{}的流程信息", deploy.getId());

        ProcessDefinition processDefinition2 = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        log.info("流程定义ID为：{}", (processDefinition2 == null ? "null" :
                processDefinition2.getId()));
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 13:57
    * @Desc: 流程定义挂起
    */
    @Test
    void testMethodSuspendProcessDefinition() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();


        RepositoryService repositoryService = engine.getRepositoryService();

        // 查询流程定义信息
        List<ProcessDefinition> leaveApplyProcess = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("LeaveApplyProcess").list();


        ProcessDefinition processDefinition1 = repositoryService.createProcessDefinitionQuery().processDefinitionId("LeaveApplyProcess:3:5004").singleResult();
        // 进行流程挂起操作
        repositoryService.suspendProcessDefinitionById(processDefinition1.getId());

        ProcessDefinition processDefinition2 = repositoryService.createProcessDefinitionQuery().processDefinitionId("LeaveApplyProcess:3:5004").singleResult();
        log.info("流程定义ID为：{}，流程定义key为：{}，是否挂起：{}", processDefinition2.getId(),
                processDefinition2.getKey(), processDefinition2.isSuspended());


    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 14:16
    * @Desc: 流程定义激活
    */
    @Test
    void testMethodActivateProcessEngine() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .suspended().list();

        // 进行激活操作
        for (ProcessDefinition processDefinition : list) {
            repositoryService.activateProcessDefinitionById(processDefinition.getId());
        }

        ProcessDefinition processDefinition1 = repositoryService.createProcessDefinitionQuery().processDefinitionId("LeaveApplyProcess:3:5004").singleResult();

        log.info("流程定义ID为{}, 流程定义key为{}, 是否挂起：{}", processDefinition1.getId(),
                processDefinition1.getKey(), processDefinition1.isSuspended());
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/4 15:08
    * @Desc: model动态创建相关流程轨迹信息
    */
    @Test
    void testMethodModel() {
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        process.setId("testModelProcess");
        process.setName("测试创建model流程");

        StartEvent startEvent = new StartEvent();
        startEvent.setId("begin");
        startEvent.setName("开始节点");
        process.addFlowElement(startEvent);


        UserTask userTask1 = new UserTask();
        userTask1.setId("user1");
        userTask1.setName("节点1");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("user2");
        userTask2.setName("节点2");
        process.addFlowElement(userTask2);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        endEvent.setName("结束节点");
        process.addFlowElement(endEvent);

        process.addFlowElement(new SequenceFlow(startEvent.getId(), userTask1.getId()));
        process.addFlowElement(new SequenceFlow(userTask1.getId(), userTask2.getId()));
        process.addFlowElement(new SequenceFlow(userTask2.getId(), endEvent.getId()));

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        repositoryService.createDeployment()
                .addBpmnModel("testModelProcess.bpmn20.xml", bpmnModel)
                .deploy();

        System.out.println();

    }

}



