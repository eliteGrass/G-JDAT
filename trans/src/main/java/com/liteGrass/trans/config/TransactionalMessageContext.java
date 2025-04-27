package com.liteGrass.trans.config;


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

import cn.hutool.extra.spring.SpringUtil;
import com.liteGrass.trans.anno.ZhlxTransactionalMessage;
import com.liteGrass.trans.config.enums.TransMessageActuatorType;
import com.liteGrass.trans.config.prop.MessageInfoProp;
import com.liteGrass.trans.logs.service.ZhlxTransLogService;
import com.liteGrass.trans.product.trans.ZhlxTransMessageProduct;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TransactionalMessageContext
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 17:47
 * @Description 上下文信息
 */
@Data
public class TransactionalMessageContext {
    private ZhlxTransactionalMessage transactionalMessage;
    private MessageInfoProp messageInfoProp;
    private ZhlxTransMessageProduct product;
    private ZhlxTransMessageProduct transactionalProduct;
    public static final Map<String, ZhlxMessageBuilderFun> MESSAGE_BUILDER_FUN_MAP = new ConcurrentHashMap<>();
    public static final Map<TransMessageActuatorType, ZhlxTransMessageProduct> TRANS_MESSAGE_ACTUATOR_MAP = new ConcurrentHashMap<>();

    public final static ZhlxTransLogService logService = SpringUtil.getBean(ZhlxTransLogService.class);


    private TransactionalMessageContext() {};

    public static TransactionalMessageContext builder(ZhlxTransactionalMessage transactionalMessage, Object... args) {
        TransactionalMessageContext transactionalMessageContext = new TransactionalMessageContext();
        transactionalMessageContext.setTransactionalMessage(transactionalMessage);
        ZhlxMessageBuilderFun zhlxMessageBuilderFun = MESSAGE_BUILDER_FUN_MAP.get(transactionalMessage.messageBuilderFunName());
        transactionalMessageContext.setMessageInfoProp(zhlxMessageBuilderFun.messageBuild(args));
        transactionalMessageContext.setTransactionalProduct(TRANS_MESSAGE_ACTUATOR_MAP.get(transactionalMessage.transActuator()));
        return transactionalMessageContext;
    }
}
