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

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;

/**
 * @ClassName DataSourceCondition
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 21:21
 * @Description 判断数据库条件
 */
public class DataSourceCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return (context.getBeanFactory().getBeanNamesForType(DataSource.class).length > 0) ?
                ConditionOutcome.match("DataSource found") :
                ConditionOutcome.noMatch("DataSource not found");
    }
}
