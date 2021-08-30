package org.starcoin.starswap.api.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.data.model.LiquidityPool;
import org.starcoin.starswap.api.data.model.LiquidityPoolId;

import java.util.List;

public interface LiquidityPoolRepository extends JpaRepository<LiquidityPool, LiquidityPoolId> {

    List<LiquidityPool> findByDeactivedIsFalse();

//
//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
