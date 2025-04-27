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

import com.liteGrass.trans.config.prop.MessageHolder;
import com.liteGrass.trans.config.prop.MessageInfoProp;

/**
 * @ClassName ZhlxMessageBuildFun
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 18:00
 * @Description 指定相应的message构造器物
 */
public interface ZhlxMessageBuilderFun {
    /**
     * 构建消息
     * @param args
     * @return
     */
    public default MessageInfoProp messageBuild(Object... args) {
        MessageHolder messageHolder = this.doMessageHolder(args);
        messageHolder.getMessageBuilder().setHeader("transId", messageHolder.getMessageInfoProp().getTransId());
        this.doMessageHeader(messageHolder, args);
        MessageInfoProp messageInfoProp = messageHolder.getMessageInfoProp();
        messageInfoProp.setMessage(messageHolder.getMessageBuilder().build());
        return messageInfoProp;
    }

    public MessageHolder doMessageHolder(Object... args);

    public default void doMessageHeader(MessageHolder messageHolder, Object... args) {
    }


    /**
     * 返回名称
     * @return
     */
    public default String getName() {
        return this.getClass().getSimpleName();
    }
}
