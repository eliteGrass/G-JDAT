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

import com.liteGrass.trans.product.trans.ZhlxRocketMqTransMessageProduct;
import com.liteGrass.trans.product.trans.ZhlxTransMessageProduct;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MqMessageConfig
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 16:22
 * @Description
 */
@AutoConfigureAfter(RocketMQAutoConfiguration.class)
@Configuration
@ComponentScan("com.liteGrass.trans")
public class MqMessageAutoConfig {

    @ConditionalOnBean(RocketMQTemplate.class)
    @Bean
    public ZhlxTransMessageProduct ZhlxRocketMqTransMessageProduct(RocketMQTemplate rocketMqTemplate) {
        return new ZhlxRocketMqTransMessageProduct(rocketMqTemplate);
    }

}
