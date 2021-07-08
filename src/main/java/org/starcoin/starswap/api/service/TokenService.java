package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.bean.Token;
import org.starcoin.starswap.api.dao.TokenRepository;

import java.util.List;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> findByDeactivedIsFalse() {
        return tokenRepository.findByDeactivedIsFalse();
    }

    public Token getToken(String tokenId) {
       return tokenRepository.findById(tokenId).orElse(null);
    }
}
