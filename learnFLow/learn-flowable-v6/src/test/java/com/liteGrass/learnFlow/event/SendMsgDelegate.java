package com.liteGrass.learnFlow.event;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @ClassName: SendMsgDelegate
 * @Author: liteGrass
 * @Date: 2025/9/2 15:36
 * @Description:
 */
public class SendMsgDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("SendMsgDelegate");
    }
}
