package org.starcoin.starswap.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.bean.LiquidityAccount;
import org.starcoin.starswap.api.bean.LiquidityAccountId;
import org.starcoin.starswap.api.bean.TokenPairPool;
import org.starcoin.starswap.api.bean.TokenPairPoolId;

import java.util.List;

public interface LiquidityAccountRepository extends JpaRepository<LiquidityAccount, LiquidityAccountId> {

    List<LiquidityAccount> findByDeactivedIsFalse();

    List<LiquidityAccount> findByLiquidityAccountIdAccountAddress(String accountAddress);

//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
