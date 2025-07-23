package com.itsoku.lesson048.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson048.enuns.AccountFundsDataBusTypeEnum;
import com.itsoku.lesson048.mapper.AccountFundsDataMapper;
import com.itsoku.lesson048.po.AccountFundsDataPO;
import com.itsoku.lesson048.service.IAccountFundsDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>description</b>：账户资金流水服务 <br>
 * <b>time</b>：2024/6/13 12:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class AccountFundsDataServiceImpl extends ServiceImpl<AccountFundsDataMapper, AccountFundsDataPO> implements IAccountFundsDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountFundsDataServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Long accountId, BigDecimal price, Long beforeAccountFundsSnapshotId, Long afterAccountFundsSnapshotId, AccountFundsDataBusTypeEnum accountFundsDataBusTypeEnum, Long busId) {
        AccountFundsDataPO appAccountFundsData = new AccountFundsDataPO();
        appAccountFundsData.setAccountId(accountId);
        appAccountFundsData.setPrice(price);
        appAccountFundsData.setBeforeAccountFundsSnapshotId(beforeAccountFundsSnapshotId);
        appAccountFundsData.setAfterAccountFundsSnapshotId(afterAccountFundsSnapshotId);
        appAccountFundsData.setIncome(accountFundsDataBusTypeEnum.getIncome().getValue());
        appAccountFundsData.setBusType(accountFundsDataBusTypeEnum.getValue());
        appAccountFundsData.setBusId(busId);
        appAccountFundsData.setCreateTime(LocalDateTime.now());
        this.save(appAccountFundsData);
    }

}
