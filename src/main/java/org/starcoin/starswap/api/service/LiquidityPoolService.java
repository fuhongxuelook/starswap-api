package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityPool;
import org.starcoin.starswap.api.data.model.LiquidityPoolId;
import org.starcoin.starswap.api.data.repo.LiquidityPoolRepository;

import java.util.List;

@Service
public class LiquidityPoolService {

    private static final Logger LOG = LoggerFactory.getLogger(LiquidityPoolService.class);

    private final LiquidityPoolRepository liquidityPoolRepository;

    @Autowired
    public LiquidityPoolService(LiquidityPoolRepository liquidityPoolRepository) {
        this.liquidityPoolRepository = liquidityPoolRepository;
    }

    public List<LiquidityPool> findByDeactivedIsFalse() {
        return liquidityPoolRepository.findByDeactivedIsFalse();
    }

    public LiquidityPool getLiquidityPool(LiquidityPoolId liquidityPoolId) {
        return liquidityPoolRepository.findById(liquidityPoolId).orElse(null);
    }
}
