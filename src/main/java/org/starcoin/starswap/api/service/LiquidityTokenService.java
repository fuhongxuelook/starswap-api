package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityToken;
import org.starcoin.starswap.api.data.model.LiquidityTokenId;
import org.starcoin.starswap.api.data.repo.LiquidityTokenRepository;

import java.util.List;

@Service
public class LiquidityTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(LiquidityTokenService.class);

    private final LiquidityTokenRepository liquidityTokenRepository;

    @Autowired
    public LiquidityTokenService(LiquidityTokenRepository liquidityTokenRepository) {
        this.liquidityTokenRepository = liquidityTokenRepository;
    }

    public List<LiquidityToken> findByDeactivedIsFalse() {
        return liquidityTokenRepository.findByDeactivedIsFalse();
    }

    public LiquidityToken getLiquidityToken(LiquidityTokenId liquidityTokenId) {
        return liquidityTokenRepository.findById(liquidityTokenId).orElse(null);
    }

    public LiquidityToken findOneByTokenIdPair(String tokenXId,String tokenYId) {
        List<LiquidityToken> liquidityTokens = liquidityTokenRepository.findByLiquidityTokenIdTokenXIdAndLiquidityTokenIdTokenYId(tokenXId, tokenYId);
        if (liquidityTokens.isEmpty()) {
            return null;
        }
        if (liquidityTokens.size() > 1) {
            throw new RuntimeException("Find more than one LiquidityToken by: " + tokenXId + ":" + tokenYId);
        }
        return liquidityTokens.get(0);
    }
}
