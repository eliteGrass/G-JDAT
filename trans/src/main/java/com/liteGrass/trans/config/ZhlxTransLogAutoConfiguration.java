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

import com.liteGrass.trans.config.prop.ZhlxTransLogProp;
import com.liteGrass.trans.logs.util.ZhlxTransLogTableGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName ZhlxTransLogAutoConfiguration
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 17:11
 * @Description
 */
@RequiredArgsConstructor
@Configuration
// 存在DataSource时生效
@ConditionalOnClass(DataSource.class)
// 确保在数据源初始化后执行
@AutoConfigureAfter(DataSource.class)
@EnableConfigurationProperties(ZhlxTransLogProp.class)
@MapperScan("com.liteGrass.trans.logs.mapper")
public class ZhlxTransLogAutoConfiguration {


    private final DataSource dataSource;
    private final ZhlxTransLogProp properties;

    @PostConstruct
    public void initLogTable() {
        ZhlxTransLogTableGenerator generator = new ZhlxTransLogTableGenerator(dataSource, properties);
        generator.createTableIfNotExists();
    }
}
