package org.starcoin.starswap.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityToken;
import org.starcoin.starswap.api.data.model.StructType;
import org.starcoin.starswap.api.data.model.Token;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigDecimal;
import java.net.MalformedURLException;

@Service
public class OnChainService {

    public static final String USD_EQUIVALENT_TOKEN_ID = "Usdx";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LiquidityTokenService liquidityTokenService;

    private JsonRpcClient jsonRpcClient;

    public OnChainService(@Value("${starcoin.json-rpc-url}") String jsonRpcUrl) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
    }

    public BigDecimal getToUsdExchangeRate(String tokenTypeTag) {
        Token usdEquivalentToken = tokenService.getTokenOrElseThrow(USD_EQUIVALENT_TOKEN_ID, () -> new RuntimeException("Cannot find USD equivalent token."));
        String usdEquivalentTokenTypeTag = usdEquivalentToken.getTokenStructType().toTypeTagString();
        StructType tokenStructType = StructType.parse(tokenTypeTag);
        Token token = tokenService.getTokenByStructType(tokenStructType);
        LiquidityToken liquidityToken = liquidityTokenService.findOneByTokenIdPair(token.getTokenId(), usdEquivalentToken.getTokenId());
        return jsonRpcClient.getExchangeRate(liquidityToken.getLiquidityTokenId().getLiquidityTokenAddress(),
                tokenTypeTag, usdEquivalentTokenTypeTag);
    }
}
