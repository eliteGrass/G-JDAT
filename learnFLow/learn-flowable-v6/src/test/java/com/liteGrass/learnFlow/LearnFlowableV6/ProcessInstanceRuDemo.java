package com.liteGrass.learnFlow.LearnFlowableV6;


import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ProcessInstanceRuDemo
 * @Author: liteGrass
 * @Date: 2025/7/31 14:24
 * @Description: 流程实例
 */
public class ProcessInstanceRuDemo {

    private static final Logger log = LoggerFactory.getLogger(ProcessInstanceRuDemo.class);

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 14:24
    * @Desc: 发起流程实例, 如果根据key进行发起的话，会发起最新版本
    */
    @Test
    void testMethodStartProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("LeaveApplyProcess");
        ProcessInstance queryPi = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(processInstance.getId())
                .singleResult();
        log.info("流程实例ID为：{}，流程定义ID为：{}，流程实例名称为：{}", queryPi.getId(),
                queryPi.getProcessDefinitionId(), queryPi.getName());


        // 根据流程定义的id进行发起
        ProcessInstance startPi = runtimeService.startProcessInstanceById(queryPi.getProcessInstanceId());
        log.info("流程实例ID为：{}，流程定义ID为：{}，流程实例名称为：{}", startPi.getId(),
                startPi.getProcessDefinitionId(), startPi.getName());


    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 16:11
    * @Desc: 测试唤醒， 唤醒的不是任务，而时
    */
    @Test
    void testMethodTriggerProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();


        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId("15005")
                .onlyChildExecutions()
                .singleResult();

        runtimeService.trigger(execution.getId());


    }

}
