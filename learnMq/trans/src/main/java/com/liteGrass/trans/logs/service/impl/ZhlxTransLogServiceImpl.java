package com.liteGrass.trans.logs.service.impl;


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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liteGrass.trans.config.enums.TransStatus;
import com.liteGrass.trans.logs.entity.ZhlxTransLogEntity;
import com.liteGrass.trans.logs.mapper.ZhlxTransLogMapper;
import com.liteGrass.trans.logs.service.ZhlxTransLogService;
import org.springframework.stereotype.Service;

/**
 * @ClassName ZhlxTransLogServiceImpl
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 22:38
 * @Description
 */
@Service
public class ZhlxTransLogServiceImpl extends ServiceImpl<ZhlxTransLogMapper, ZhlxTransLogEntity> implements ZhlxTransLogService {

    @Override
    public void saveLog(ZhlxTransLogEntity transLog) {
        transLog.setTransStatus(TransStatus.INIT.toString());
        this.save(transLog);
    }

    @Override
    public void updateTransStatus(String transId, String transStatus) {
        LambdaUpdateWrapper<ZhlxTransLogEntity> update = Wrappers.<ZhlxTransLogEntity>lambdaUpdate()
                .eq(ZhlxTransLogEntity::getTransId, transId)
                .set(ZhlxTransLogEntity::getTransStatus, transStatus);
        this.update(update);
    }

    @Override
    public String getTransStatus(String transId) {
        LambdaQueryWrapper<ZhlxTransLogEntity> wrapper = Wrappers.<ZhlxTransLogEntity>lambdaQuery().eq(ZhlxTransLogEntity::getTransId, transId);
        ZhlxTransLogEntity transLog = this.getOne(wrapper);
        return transLog.getTransStatus();
    }



}
