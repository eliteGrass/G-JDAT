package com.liteGrass.trans.config.prop;


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

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.springframework.messaging.Message;

/**
 * @ClassName MessageInfoProp
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 23:46
 * @Description
 */
@Data
@Accessors(chain = true)
public class MessageInfoProp {
    public static final String DEFAULT_SELECTOR_EXPRESSION = "*";

    private String topic;

    private SelectorType selectorType = SelectorType.TAG;

    private String selectorExpression = DEFAULT_SELECTOR_EXPRESSION;

    private boolean isOrder =  true;

    private String transId;

    private String fifoMesGroupKey;

    private String messageBuilderFunName;

    private Message message;
}
