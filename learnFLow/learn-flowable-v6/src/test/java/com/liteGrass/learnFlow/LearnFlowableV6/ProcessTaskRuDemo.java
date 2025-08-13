package com.liteGrass.learnFlow.LearnFlowableV6;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.TreeValueExpression;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.job.api.Job;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ProcessTaskRuDemo
 * @Author: liteGrass
 * @Date: 2025/7/31 16:52
 * @Description: 流程任务
 */
public class ProcessTaskRuDemo {

    private static final Logger log = LoggerFactory.getLogger(ProcessTaskRuDemo.class);

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 16:52
    * @Desc: 流程任务
    */
    @Test
    void testMethodQueryTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned("shenqingren").orderByTaskCreateTime().desc().list();

        for (Task task : tasks) {
            log.info("查询{}的待办任务, 创建时间为: {}, 任务id是: {}", task.getAssignee(), task.getCreateTime(), task.getProcessDefinitionId());
            taskService.complete(task.getId());
        }
        System.out.println();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 22:15
    * @Desc: 流程任务办理与权限控制
    */
    @Test
    void testMethodTaskHandle() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

       /* RepositoryService repositoryService = processEngine.getRepositoryService();
        //部署流程
        repositoryService.createDeployment()
                .addClasspathResource("processes/LeaveApplyProcess.bpmn20.xml")
                .deploy();

        // 流程运行时服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //启动流程任务
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("LeaveApplyProcess");
*/

        RuntimeService runtimeService = processEngine.getRuntimeService();

        TaskService taskService = processEngine.getTaskService();
        // 获取第一个流程任务
        Task task = taskService.createTaskQuery().processDefinitionId("LeaveApplyProcess:4:25004").singleResult();
        // 获取第一个任务，第一个任务已经指定人员，指定人员的任务，不能进行重复认领操作, 需要取消认领后，在进行认领操作
        /*taskService.addCandidateUser(task.getId(), "zhangsan");
        taskService.addUserIdentityLink(task.getId(), "lisi", "participant");*/
        /*taskService.unclaim(task.getId());
        // 认领过后，该流程就属于张三的了，不能被其他流程在进行认领， 参与者是否也能认领任务
        taskService.claim(task.getId(), "lisi");*/
//
//
//        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
//        System.out.println(task.getName());
        taskService.claim(task.getId(), "lisi");
        Map<String, Object> variables = new HashMap<>();
        variables.put("task_审批_outcome", "agree");
        taskService.complete(task.getId(), variables);
        System.out.println();
    }




    /**
    * @Auther: liteGrass
    * @Date: 2025/8/1 6:46
    * @Desc: 评论及附件功能进行测试
    */
    @Test
    void testMethodCommentAndAttach() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        // 获取第一个流程任务
        Task task = taskService.createTaskQuery().processDefinitionId("LeaveApplyProcess:4:25004").singleResult();

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "测试评论内容1");
        // 进行查询评论内容
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            taskService.deleteComment(taskComment.getId());
        }


        Attachment attachment = taskService.createAttachment("", task.getId(), task.getProcessInstanceId(), "测试附件", "描述",
                ClassLoaderUtil.getClassLoader().getResourceAsStream("processes/LeaveApplyProcess.bpmn20.xml"));

        for (Attachment taskAttachment : taskService.getTaskAttachments(task.getId())) {
            taskService.deleteAttachment(taskAttachment.getId());
        }
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/1 9:56
    * @Desc: 定时器进行执行
    */
    @Test
    void testMethodTimeExec() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/TestTimeFlow.bpmn20.xml").deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TestTimeFlow");

        TaskService taskService = processEngine.getTaskService();

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        // 进行认领， 没有进行认领也是可以进行执行的， 执行后，执行人为null而已
        taskService.complete(task.getId());

        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getId()).list();
        for (HistoricTaskInstance hisTaskInst : list) {
            log.info("ID为{}的任务的办理人为{}，开始时间{}，结束时间{}", hisTaskInst.getId(),
                    hisTaskInst.getAssignee(),
                    hisTaskInst.getCreateTime(),
                    hisTaskInst.getCreateTime()
            );
        }

        // 查询定时执行的任务
        ManagementService managementService = processEngine.getManagementService();
        List<Job> taskJobList = managementService.createTimerJobQuery().processInstanceId(processInstance.getId()).list();
        for (Job job : taskJobList) {
            log.info("定时器任务{}的类型为{}，执行时间为{}", job.getId(),job.getJobType(),
                    DateUtil.formatDateTime(job.getDuedate()));
            // 转换为执行队列
            managementService.moveTimerToExecutableJob(job.getId());
            log.info("立即执行定时器任务{}", job.getId());
            // 执行完成后，移动到历史记录队列
            managementService.executeJob(job.getId());
        }

        // 查询历史定时任务执行
        System.out.println();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/8/1 10:33
    * @Desc: command命令执行
    */
    @Test
    void testMethodCommandExec() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        Date date = new Date();
        log.info("当前时间为:{}", date);
        String express = "${dateFormat.format(date)}";
        Map<String, Object> varibaleMap = MapUtil.<String, Object>builder()
                .put("date", date)
                .put("dateFormat", DatePattern.NORM_DATETIME_FORMAT)
                .build();

        ManagementService managementService = processEngine.getManagementService();
        Object res = managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ExpressionFactoryImpl expressionFactory = new ExpressionFactoryImpl();
                SimpleContext simpleContext = new SimpleContext();
                for (String key : varibaleMap.keySet()) {
                    simpleContext.setVariable(
                            key,
                            expressionFactory.createValueExpression(varibaleMap.get(key), varibaleMap.get(key).getClass()));
                }
                // 最后执行
                TreeValueExpression valueExpression = expressionFactory.createValueExpression(simpleContext, express, Object.class);
                return valueExpression.getValue(simpleContext);
            }
        });
        log.info("格式化后：{}", res);
    }



}
