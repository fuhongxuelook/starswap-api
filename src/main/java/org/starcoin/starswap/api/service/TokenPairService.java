package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.bean.TokenPair;
import org.starcoin.starswap.api.bean.TokenPairId;
import org.starcoin.starswap.api.dao.TokenPairRepository;

import java.util.List;

@Service
public class TokenPairService {

    private static final Logger logger = LoggerFactory.getLogger(TokenPairService.class);

    private final TokenPairRepository tokenPairRepository;

    @Autowired
    public TokenPairService(TokenPairRepository tokenPairRepository) {
        this.tokenPairRepository = tokenPairRepository;
    }

    public List<TokenPair> findByDeactivedIsFalse() {
        return tokenPairRepository.findByDeactivedIsFalse();
    }

    public TokenPair getTokenPair(TokenPairId tokenPairId) {
        return tokenPairRepository.findById(tokenPairId).orElse(null);
    }
}
