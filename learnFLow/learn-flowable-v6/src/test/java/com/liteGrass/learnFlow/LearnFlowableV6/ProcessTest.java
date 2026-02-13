package com.liteGrass.learnFlow.LearnFlowableV6;


import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;

/**
 * @ClassName: ProcessTest
 * @Author: liteGrass
 * @Date: 2025/9/2 6:34
 * @Description: 流程进行完整测试验证
 */
public class ProcessTest {

    /**
    * @Auther: liteGrass
    * @Date: 2025/9/2 6:34
    * @Desc: 流程进行测试
    */
    @Test
    void testMethodExec() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/learnExec.bpmn20.xml")
                .deploy();
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("learnExec");


        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());



    }

}
