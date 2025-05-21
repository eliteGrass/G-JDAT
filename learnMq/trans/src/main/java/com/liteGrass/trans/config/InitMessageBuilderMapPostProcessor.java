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

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import com.liteGrass.trans.anno.Actuator;
import com.liteGrass.trans.config.enums.TransMessageActuatorType;
import com.liteGrass.trans.product.trans.ZhlxTransMessageProduct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @ClassName InitMessageBuilderMapPostProcessor
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 20:23
 * @Description
 */
@Component
public class InitMessageBuilderMapPostProcessor implements BeanPostProcessor {

    /**
     * 后置初始化相关数据
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ZhlxMessageBuilderFun) {
            ZhlxMessageBuilderFun fun = (ZhlxMessageBuilderFun) bean;
            TransactionalMessageContext.MESSAGE_BUILDER_FUN_MAP.put(fun.getName(), fun);
        }
        if (bean instanceof ZhlxTransMessageProduct) {
            TransMessageActuatorType annotationValue = AnnotationUtil.getAnnotationValue(bean.getClass(), Actuator.class);
            if (ObjectUtil.isNotNull(annotationValue)) {
                TransactionalMessageContext.TRANS_MESSAGE_ACTUATOR_MAP.put(annotationValue, (ZhlxTransMessageProduct) bean);
            }
        }
        return bean;
    }
}
