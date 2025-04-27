package com.liteGrass.trans.advisor;


/**
 * Copyright (c) 2024 Huahui Information Technology Co., LTD.
 * and China Nuclear Engineering & Construction Corporation Limited (Loongxin Authors).
 * All Rights Reserved.
 * <p>
 * This software is part of the Zhonghe Loongxin Development Platform (the "Platform").
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <p>
 * For more information about the Platform, terms and conditions, and user licenses,
 * please visit our official website at www.icnecc.com.cn or contact us directly.
 */

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.liteGrass.trans.anno.ZhlxTransactionalMessage;
import com.liteGrass.trans.config.TransactionalMessageContext;
import com.liteGrass.trans.config.enums.TransStatus;
import com.liteGrass.trans.logs.entity.ZhlxTransLogEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName EnhanceRocketMqTransMessageDelegate
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 16:19
 * @Description 增强事务方法
 */
@Aspect
@RocketMQTransactionListener
public class EnhanceRocketMqTransMessageAdvisor extends TransMessageAdvisor implements  RocketMQLocalTransactionListener {



    @Override
    @Around("@annotation(txMsg)")
    public Object wrapWithTransaction(ProceedingJoinPoint joinPoint, ZhlxTransactionalMessage txMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //进行日志记录工作
        TransactionalMessageContext context = TransactionalMessageContext.builder(txMsg, joinPoint.getArgs());
        TransactionalMessageContext.logService.saveLog( new ZhlxTransLogEntity()
                .setMethodName(joinPoint.getSignature().getName())
                .setMethodParams(JSONUtil.toJsonStr(joinPoint.getArgs()))
                .setTransId(context.getMessageInfoProp().getTransId()));
        try {
            // 发送半消息
            ORIGINAL_METHOD.set(joinPoint);
            context.getTransactionalProduct().sendMsgTransactionAndOrder(context);
            if (ObjectUtil.equals(ORIGINAL_RES.get(), "error")) {
                throw new RuntimeException("异常");
            }
            // 修改日志状态工作
            return ORIGINAL_RES.get();
        } catch (Throwable e) {
            // 修改日志状态工作
            TransactionalMessageContext.logService.updateTransStatus(context.getMessageInfoProp().getTransId(), TransStatus.ROLLBACK.toString());
            throw new RuntimeException(e);
        } finally {
            ORIGINAL_METHOD.remove();
            ORIGINAL_RES.remove();
        }
    }


    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            //保存日志，状态为等待
            TransactionalMessageContext.logService.updateTransStatus(o.toString(), TransStatus.PROCESSING.toString());
            // 执行本地事务（如已通过@Transactional处理，此处可标记为UNKNOWN）
            System.out.println("开始执行executeLocalTransaction");
            Object res = ORIGINAL_METHOD.get().proceed();
            ORIGINAL_RES.set(res);
            //修改日志状态为成功
            TransactionalMessageContext.logService.updateTransStatus(o.toString(), TransStatus.COMMIT.toString());
            System.out.println("------------- executeLocalTransaction");
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            ORIGINAL_RES.set("error");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        System.out.println("------------- checkLocalTransaction");
        MessageHeaders headers = message.getHeaders();
        String transId = headers.get("transId").toString();
        String transStatus = TransactionalMessageContext.logService.getTransStatus(transId);
        if (ObjectUtil.equals(transStatus, TransStatus.ROLLBACK.toString())) {
            return RocketMQLocalTransactionState.ROLLBACK;
        } else if (ObjectUtil.equals(transStatus, TransStatus.COMMIT.toString())) {
            System.out.println("------------- checkLocalTransaction --- commit");
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            System.out.println("------------- checkLocalTransaction --- UNKNOWN");
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }


}
