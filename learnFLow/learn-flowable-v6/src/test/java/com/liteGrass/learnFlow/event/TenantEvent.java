package com.liteGrass.learnFlow.event;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @ClassName: TenantEvent
 * @Author: liteGrass
 * @Date: 2025/9/2 8:35
 * @Description:
 */
public class TenantEvent implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("执行相关租户问题");
    }
}
