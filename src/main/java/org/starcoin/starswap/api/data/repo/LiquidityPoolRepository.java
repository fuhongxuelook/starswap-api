package org.starcoin.starswap.api.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.starcoin.starswap.api.data.model.LiquidityPool;
import org.starcoin.starswap.api.data.model.LiquidityPoolId;

import java.util.List;

public interface LiquidityPoolRepository extends JpaRepository<LiquidityPool, LiquidityPoolId> {

    List<LiquidityPool> findByDeactivedIsFalse();

    @Query(value = "select * from liquidity_pool p where token_x_id = :tokenXId and token_y_id = :tokenYId", nativeQuery = true)
    List<LiquidityPool> findByLiquidityPoolIdTokenXIdAndLiquidityPoolIdTokenYId(String tokenXId, String tokenYId);

//
//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
}
