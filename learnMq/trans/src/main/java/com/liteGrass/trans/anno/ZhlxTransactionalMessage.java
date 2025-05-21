package com.liteGrass.trans.anno;


/**
 * Copyright (c) 2024 Huahui Information Technology Co., LTD.
 * and China Nuclear Engineering & Construction Corporation Limited (Loongxin Authors).
 * All Rights Reserved.
 *
 * This software is part of the Zhonghe Loongxin Development Platform (the "Platform").
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * For more information about the Platform, terms and conditions, and user licenses,
 * please visit our official website at www.icnecc.com.cn or contact us directly.
 */

import com.liteGrass.trans.config.enums.TransMessageActuatorType;

import java.lang.annotation.*;

/**
 * @ClassName ZhlxTransactionalMessage
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 10:26
 * @Description 事务注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZhlxTransactionalMessage {

    String messageBuilderFunName() default "";

    TransMessageActuatorType transActuator() default TransMessageActuatorType.ROCKET_MQ;
}
