package com.liteGrass.funs;


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

import cn.hutool.core.util.IdUtil;
import com.liteGrass.trans.config.ZhlxMessageBuilderFun;
import com.liteGrass.trans.config.prop.MessageHolder;
import com.liteGrass.trans.config.prop.MessageInfoProp;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestFuns
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 23:58
 * @Description
 */
@Component
public class TestFuns implements ZhlxMessageBuilderFun {

    @Override
    public MessageHolder doMessageHolder(Object... args) {
        MessageInfoProp messageInfoProp = new MessageInfoProp()
                .setTopic("service-flow-action-status-fifo:AGREE_BUTTON")
                .setFifoMesGroupKey("11")
                .setMessageBuilderFunName(getName())
                .setTransId(IdUtil.simpleUUID());
        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(args[0]);
        return MessageHolder.builder()
                .messageBuilder(messageBuilder)
                .messageInfoProp(messageInfoProp)
                .build();
    }

    @Override
    public void doMessageHeader(MessageHolder messageHolder, Object... args) {
        messageHolder.getMessageBuilder()
                .setHeader(RocketMQHeaders.TAGS, "AGREE_BUTTON")
                .setHeader("FLOW_ACTION", "AGREE_BUTTON")
                .setHeader("BEFORE_FLOW_STATUS", "AGREE");
    }

    @Override
    public String getName() {
        return "TestFuns";
    }
}
