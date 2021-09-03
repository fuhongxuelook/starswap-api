package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityTokenFarmAccount;
import org.starcoin.starswap.api.data.model.LiquidityTokenFarmAccountId;
import org.starcoin.starswap.api.data.repo.FarmAccountRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Service
public class FarmAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmAccountService.class);

    @Autowired
    private FarmAccountRepository farmAccountRepository;


    public List<LiquidityTokenFarmAccount> findByAccountAddress(String accountAddress) {
        return farmAccountRepository.findByFarmAccountIdAccountAddress(accountAddress);
    }

    @Transactional
    public LiquidityTokenFarmAccount activeFarmAccount(LiquidityTokenFarmAccountId farmAccountId) {
        LiquidityTokenFarmAccount farmAccount = this.farmAccountRepository.findById(farmAccountId).orElse(null);
        if (farmAccount == null) {
            farmAccount = new LiquidityTokenFarmAccount();
            farmAccount.setFarmAccountId(farmAccountId);
            farmAccount.setStakeAmount(BigInteger.ZERO);//todo ???
            farmAccount.setDeactived(false);
            farmAccount.setCreatedAt(Instant.now().getEpochSecond());
            farmAccount.setCreatedBy("admin");
            farmAccount.setUpdatedAt(farmAccount.getCreatedAt());
            farmAccount.setUpdatedBy("admin");
        } else {
            farmAccount.setUpdatedAt(Instant.now().getEpochSecond());
            farmAccount.setUpdatedBy("admin");
            farmAccount.setDeactived(false);
        }
        this.farmAccountRepository.save(farmAccount);
        return farmAccount;
    }

}
