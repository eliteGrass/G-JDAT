package com.liteGrass.trans.product.trans;


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

import com.liteGrass.trans.config.TransactionalMessageContext;
import com.liteGrass.trans.product.ZhlxMqProduct;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @ClassName ZhlxTransMessageProduct
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 16:15
 * @Description 事务消息
 */
public abstract class ZhlxTransMessageProduct implements ZhlxMqProduct {

    public abstract SendResult sendMsgTransactionAndOrder(TransactionalMessageContext context) throws MQClientException;

}
