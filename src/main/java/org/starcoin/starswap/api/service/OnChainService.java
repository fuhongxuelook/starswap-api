package org.starcoin.starswap.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;

@Service
public class OnChainService {

    public static final String USD_EQUIVALENT_TOKEN_ID = "Usdx"; //todo config???

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LiquidityTokenService liquidityTokenService;

    @Autowired
    private LiquidityTokenFarmService liquidityTokenFarmService;

    private final JsonRpcClient jsonRpcClient;

    public OnChainService(@Value("${starcoin.json-rpc-url}") String jsonRpcUrl) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
    }

    public Pair<BigInteger, BigInteger> getFarmStakedReservesByTokenIdPair(String tokenXId, String tokenYId) {
        //LiquidityToken liquidityToken = liquidityTokenService.findOneByTokenIdPair(tokenXId, tokenYId);
        Token tokenX = tokenService.getToken(tokenXId);
        Token tokenY = tokenService.getToken(tokenYId);
        LiquidityTokenFarm liquidityTokenFarm = liquidityTokenFarmService.findOneByTokenIdPair(tokenXId, tokenYId);
        LiquidityTokenFarmId liquidityTokenFarmId = liquidityTokenFarm.getLiquidityTokenFarmId();
        return jsonRpcClient.getTokenSwapFarmStakedReserves(liquidityTokenFarmId.getFarmAddress(),
                liquidityTokenFarmId.getLiquidityTokenId().getLiquidityTokenAddress(),
                tokenX.getTokenStructType().toTypeTagString(),
                tokenY.getTokenStructType().toTypeTagString());
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
