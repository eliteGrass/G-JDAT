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
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName TransLogProp
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 17:12
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "config.trans-log")
public class ZhlxTransLogProp {
    /**
     * 是否启用日志表自动创建（默认true）
     */
    private boolean enabled = true;

    private String tableName = "zhlx_trans_log";
}
