package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.TokenPairPool;
import org.starcoin.starswap.api.data.model.TokenPairPoolId;
import org.starcoin.starswap.api.data.repo.TokenPairPoolRepository;

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
