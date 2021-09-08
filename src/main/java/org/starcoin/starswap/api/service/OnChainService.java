package org.starcoin.starswap.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.MalformedURLException;

@Service
public class OnChainService {

    public static final String USD_EQUIVALENT_TOKEN_ID = "Usdx"; //todo config???

    private final JsonRpcClient jsonRpcClient;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LiquidityTokenService liquidityTokenService;

    @Autowired
    private LiquidityTokenFarmService liquidityTokenFarmService;

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

    public BigDecimal getFarmEstimatedApy(LiquidityTokenFarm liquidityTokenFarm) {
        Token tokenX = tokenService.getTokenOrElseThrow(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId(), () -> new RuntimeException("Cannot find Token by Id"));
        Token tokenY = tokenService.getTokenOrElseThrow(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId(), () -> new RuntimeException("Cannot find Token by Id"));
        return getFarmEstimatedApy(tokenX, tokenY, liquidityTokenFarm);
    }

    public BigDecimal getFarmEstimatedApyByTokenIdPair(String tokenXId, String tokenYId) {
        Token tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> new RuntimeException("Cannot find Token by Id: " + tokenXId));
        Token tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> new RuntimeException("Cannot find Token by Id: " + tokenYId));
        LiquidityTokenFarm liquidityTokenFarm = liquidityTokenFarmService.findOneByTokenIdPair(tokenX.getTokenId(), tokenY.getTokenId());
        if (liquidityTokenFarm == null) {
            throw new RuntimeException("Cannot find LiquidityTokenFarm by tokenId pair: " + tokenXId + " / " + tokenYId);
        }
        return getFarmEstimatedApy(tokenX, tokenY, liquidityTokenFarm);
    }

    private BigDecimal getFarmEstimatedApy(Token tokenX, Token tokenY, LiquidityTokenFarm liquidityTokenFarm) {
        if (!tokenX.getTokenId().equals(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId())
                || !tokenY.getTokenId().equals(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId())) {
            throw new IllegalArgumentException("Token Id in token object NOT equals token id in farm object.");
        }
        String farmAddress = liquidityTokenFarm.getLiquidityTokenFarmId().getFarmAddress();
        String liquidityTokenAddress = liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getLiquidityTokenAddress();
        Pair<BigInteger, BigInteger> reservePair = jsonRpcClient.getTokenSwapFarmStakedReserves(
                farmAddress,
                liquidityTokenAddress,
                tokenX.getTokenStructType().toTypeTagString(),
                tokenY.getTokenStructType().toTypeTagString());
        BigInteger tokenXScalingFactor = jsonRpcClient.tokenGetScalingFactor(tokenX.getTokenStructType().toTypeTagString());
        BigInteger tokenYScalingFactor = jsonRpcClient.tokenGetScalingFactor(tokenY.getTokenStructType().toTypeTagString());
        BigDecimal tokenXToUsdRate = getToUsdExchangeRate(tokenX);
        BigDecimal tokenYToUsdRate = getToUsdExchangeRate(tokenY);
        BigDecimal tokenXReserveInUsd = new BigDecimal(reservePair.getItem1())
                .divide(new BigDecimal(tokenXScalingFactor), tokenXScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(tokenXToUsdRate);
        BigDecimal tokenYReserveInUsd = new BigDecimal(reservePair.getItem2())
                .divide(new BigDecimal(tokenYScalingFactor), tokenYScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(tokenYToUsdRate);

        BigInteger rewardReleasePerSecond = jsonRpcClient.tokenSwapFarmQueryReleasePerSecond(farmAddress,
                tokenX.getTokenStructType().toTypeTagString(),
                tokenY.getTokenStructType().toTypeTagString());
        BigInteger rewardPerYear = rewardReleasePerSecond.multiply(BigInteger.valueOf(60L * 60 * 24 * 365));

        Token rewardToken = tokenService.getTokenOrElseThrow(liquidityTokenFarm.getRewardTokenId(),
                () -> new RuntimeException("Cannot find Token by Id: " + liquidityTokenFarm.getRewardTokenId()));
        BigInteger rewardTokenScalingFactor = jsonRpcClient.tokenGetScalingFactor(rewardToken.getTokenStructType().toTypeTagString());
        BigDecimal rewardTokenToUsdRate = getToUsdExchangeRate(rewardToken);
        BigDecimal rewardPerYearInUsd = new BigDecimal(rewardPerYear)
                .divide(new BigDecimal(rewardTokenScalingFactor), rewardTokenScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(rewardTokenToUsdRate);

        int scale = Math.max(tokenXScalingFactor.toString().length(), tokenYScalingFactor.toString().length()) - 1;
        return rewardPerYearInUsd
                .divide(tokenXReserveInUsd.add(tokenYReserveInUsd), scale, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getToUsdExchangeRate(String tokenTypeTag) {
        StructType tokenStructType = StructType.parse(tokenTypeTag);
        Token token = tokenService.getTokenByStructType(tokenStructType);
        return getToUsdExchangeRate(token);
    }

    public BigDecimal getToUsdExchangeRateByTokenId(String tokenId) {
        Token token = tokenService.getTokenOrElseThrow(tokenId, () -> new RuntimeException("Cannot find Token by Id: " + tokenId));
        return getToUsdExchangeRate(token);
    }

    private BigDecimal getToUsdExchangeRate(Token token) {
        Token usdEquivalentToken = tokenService.getTokenOrElseThrow(USD_EQUIVALENT_TOKEN_ID, () -> new RuntimeException("Cannot find USD equivalent token."));
        String usdEquivalentTokenTypeTag = usdEquivalentToken.getTokenStructType().toTypeTagString();
        LiquidityToken liquidityToken = liquidityTokenService.findOneByTokenIdPair(token.getTokenId(), usdEquivalentToken.getTokenId());
        if (liquidityToken == null) {
            throw new RuntimeException("Cannot find LiquidityToken by tokenId pair: " + token.getTokenId() + " / " + usdEquivalentToken.getTokenId());
        }
        return jsonRpcClient.getExchangeRate(liquidityToken.getLiquidityTokenId().getLiquidityTokenAddress(),
                token.getTokenStructType().toTypeTagString(), usdEquivalentTokenTypeTag);
    }
}
