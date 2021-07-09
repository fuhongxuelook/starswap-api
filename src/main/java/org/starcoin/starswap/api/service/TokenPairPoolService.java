package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.bean.TokenPair;
import org.starcoin.starswap.api.bean.TokenPairId;
import org.starcoin.starswap.api.bean.TokenPairPool;
import org.starcoin.starswap.api.bean.TokenPairPoolId;
import org.starcoin.starswap.api.dao.TokenPairPoolRepository;
import org.starcoin.starswap.api.dao.TokenPairRepository;

import java.util.List;

@Service
public class TokenPairPoolService {

    private static final Logger logger = LoggerFactory.getLogger(TokenPairPoolService.class);

    private final TokenPairPoolRepository tokenPairPoolRepository;

    @Autowired
    public TokenPairPoolService(TokenPairPoolRepository tokenPairPoolRepository) {
        this.tokenPairPoolRepository = tokenPairPoolRepository;
    }

    public List<TokenPairPool> findByDeactivedIsFalse() {
        return tokenPairPoolRepository.findByDeactivedIsFalse();
    }

    public TokenPairPool getTokenPairPool(TokenPairPoolId tokenPairPoolId) {
        return tokenPairPoolRepository.findById(tokenPairPoolId).orElse(null);
    }
}
