package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.StructType;
import org.starcoin.starswap.api.data.model.Token;
import org.starcoin.starswap.api.data.repo.TokenRepository;

import java.util.List;
import java.util.function.Supplier;

@Service
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

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

    public Token getTokenOrElseThrow(String tokenId, Supplier<RuntimeException> exceptionSupplier) {
        return tokenRepository.findById(tokenId).orElseThrow(exceptionSupplier);
    }

    public Token getTokenByStructType(String address, String module, String name) {
        return tokenRepository.findFirstByTokenStructType(new StructType(address, module, name));
    }

    public Token getTokenByStructType(StructType structType) {
        return tokenRepository.findFirstByTokenStructType(structType);
    }
}
