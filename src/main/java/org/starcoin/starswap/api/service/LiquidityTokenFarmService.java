package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityTokenFarm;
import org.starcoin.starswap.api.data.repo.LiquidityTokenFarmRepository;

import java.util.List;

@Service
public class LiquidityTokenFarmService {

    private static final Logger LOG = LoggerFactory.getLogger(LiquidityTokenFarmService.class);

    @Autowired
    private LiquidityTokenFarmRepository liquidityTokenFarmRepository;

    public List<LiquidityTokenFarm> findByDeactivedIsFalse() {
        return liquidityTokenFarmRepository.findByDeactivedIsFalse();
    }

    public LiquidityTokenFarm findOneByTokenIdPair(String tokenXId, String tokenYId) {
        List<LiquidityTokenFarm> liquidityTokenFarms = liquidityTokenFarmRepository.findByLiquidityTokenFarmIdTokenXIdAndLiquidityTokenFarmIdTokenYId(tokenXId, tokenYId);
        if (liquidityTokenFarms.isEmpty()) {
            return null;
        }
        if (liquidityTokenFarms.size() > 1) {
            throw new RuntimeException("Find more than one LiquidityPool by: " + tokenXId + ":" + tokenYId);
        }
        return liquidityTokenFarms.get(0);
    }
}
