package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.bean.LiquidityAccount;
import org.starcoin.starswap.api.bean.LiquidityAccountId;
import org.starcoin.starswap.api.dao.LiquidityAccountRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Service
public class LiquidityAccountService {

    private static final Logger logger = LoggerFactory.getLogger(LiquidityAccountService.class);

    private final LiquidityAccountRepository liquidityAccountRepository;

    @Autowired
    public LiquidityAccountService(LiquidityAccountRepository liquidityAccountRepository) {
        this.liquidityAccountRepository = liquidityAccountRepository;
    }

    public List<LiquidityAccount> findByAccountAddress(String accountAddress) {
        return this.liquidityAccountRepository.findByLiquidityAccountIdAccountAddress(accountAddress);
    }

    @Transactional
    public LiquidityAccount activeLiquidityAccount(LiquidityAccountId liquidityAccountId) {
        LiquidityAccount liquidityAccount = this.liquidityAccountRepository.findById(liquidityAccountId).orElse(null);
        if (liquidityAccount == null) {
            liquidityAccount = new LiquidityAccount();
            liquidityAccount.setLiquidityAccountId(liquidityAccountId);
            liquidityAccount.setLiquidity(BigInteger.ZERO);//todo ???
            liquidityAccount.setDeactived(false);
            liquidityAccount.setCreatedAt(Instant.now().getEpochSecond());
            liquidityAccount.setCreatedBy("admin");
            liquidityAccount.setUpdatedAt(liquidityAccount.getCreatedAt());
            liquidityAccount.setUpdatedBy("admin");
        } else {
            liquidityAccount.setUpdatedAt(Instant.now().getEpochSecond());
            liquidityAccount.setUpdatedBy("admin");
            liquidityAccount.setDeactived(false);
        }
        this.liquidityAccountRepository.save(liquidityAccount);
        return liquidityAccount;
    }
}