package com.liteGrass.demo;


import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @Description 事务监听
 * @Author liteGrass
 * @Date 2024/12/9 18:05
 */
@RequiredArgsConstructor
/*@Aspect
@RocketMQTransactionListener*/
public class SpringBootTransChecker implements RocketMQLocalTransactionListener {

    private final ZhlxRocketMqProduct zhlxRocketMqProduct;
    public final static ThreadLocal<ProceedingJoinPoint> ORIGINAL_METHOD = new ThreadLocal<>();
    public final static ThreadLocal<Object> ORIGINAL_RES = new ThreadLocal<>();


    @Around("@annotation(txMsg)")
    public Object wrapWithTransaction(ProceedingJoinPoint joinPoint, TransactionalMessage txMsg) {
        try {
            // 发送半消息
            ORIGINAL_METHOD.set(joinPoint);
            String queue = joinPoint.getArgs()[0].toString();
            System.out.println(Thread.currentThread().getName());
            zhlxRocketMqProduct.sendMsgTransactionAndOrder("test_trans_topic", MessageBuilder.withPayload("111").build(),
                    "123456", queue.toString());
            if (ObjectUtil.equals(ORIGINAL_RES.get(), "error")) {
                throw new RuntimeException("异常");
            }
            return ORIGINAL_RES.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            // TODO 保存日志，状态为等待
            // 执行本地事务（如已通过@Transactional处理，此处可标记为UNKNOWN）
            System.out.println("开始执行executeLocalTransaction");
            Object res = ORIGINAL_METHOD.get().proceed();
            ORIGINAL_RES.set(res);
            // TODO 修改日志状态为成功
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            ORIGINAL_RES.set("error");
            // TODO 修改日志状态为异常
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // TODO 根据日志状态查询事务状态
        System.out.println("进行事务回查");
        boolean isSuccess = true;
        return isSuccess ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }
}