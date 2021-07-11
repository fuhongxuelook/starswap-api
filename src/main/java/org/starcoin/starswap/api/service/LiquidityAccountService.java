package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.bean.LiquidityAccount;
import org.starcoin.starswap.api.dao.LiquidityAccountRepository;

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

}
