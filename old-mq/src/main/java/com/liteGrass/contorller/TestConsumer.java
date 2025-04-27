package com.liteGrass.contorller;


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

import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName TestConsumer
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/26 11:40
 * @Description
 */
@Component
@RocketMQMessageListener(topic = "service-flow-action-status-fifo",
        consumerGroup = "service-flow-syncFormData-group", selectorExpression = "AGREE_BUTTON||SUBMIT_BUTTON", selectorType = SelectorType.TAG)
public class TestConsumer   implements RocketMQListener<MessageExt>  {
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(StrUtil.format("进行消息消费:{}", StrUtil.str(messageExt.getBody(), StandardCharsets.UTF_8)));
    }
}
