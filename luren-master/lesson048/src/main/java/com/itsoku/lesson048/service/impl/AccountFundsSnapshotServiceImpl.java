package com.itsoku.lesson048.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson048.mapper.AccountFundsSnapshotMapper;
import com.itsoku.lesson048.po.AccountFundsPO;
import com.itsoku.lesson048.po.AccountFundsSnapshotPO;
import com.itsoku.lesson048.service.IAccountFundsSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>description</b>：账户资金快照服务 <br>
 * <b>time</b>：2024/6/13 12:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class AccountFundsSnapshotServiceImpl extends ServiceImpl<AccountFundsSnapshotMapper, AccountFundsSnapshotPO> implements IAccountFundsSnapshotService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountFundsSnapshotServiceImpl.class);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountFundsSnapshotPO insert(AccountFundsPO accountFundsPO) {
        AccountFundsSnapshotPO appAccountFunds = new AccountFundsSnapshotPO();
        appAccountFunds.setAccountId(accountFundsPO.getAccountId());
        appAccountFunds.setBalance(accountFundsPO.getBalance());
        appAccountFunds.setFrozen(accountFundsPO.getFrozen());
        appAccountFunds.setCreateTime(LocalDateTime.now());
        this.save(appAccountFunds);
        return appAccountFunds;
    }
}
