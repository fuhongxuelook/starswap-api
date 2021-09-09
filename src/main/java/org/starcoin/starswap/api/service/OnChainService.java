package org.starcoin.starswap.api.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(OnChainService.class);

    public static final String USD_EQUIVALENT_TOKEN_ID = "Usdx"; //todo config???

    private final JsonRpcClient jsonRpcClient;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LiquidityTokenService liquidityTokenService;

    @Autowired
    private LiquidityTokenFarmService liquidityTokenFarmService;

    @Autowired
    private TokenPriceService tokenPriceService;

    public OnChainService(@Value("${starcoin.json-rpc-url}") String jsonRpcUrl) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
    }

    public BigInteger getFarmTotalStakeAmount(LiquidityTokenFarm farm) {
        String farmAddress = farm.getLiquidityTokenFarmId().getFarmAddress();
        String tokenXId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId();
        String tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> { throw new RuntimeException("Cannot find token by Id: " + tokenXId);}).getTokenStructType().toTypeTagString();
        String tokenYId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId();
        String tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> { throw new RuntimeException("Cannot find token by Id: " + tokenYId);}).getTokenStructType().toTypeTagString();
        BigInteger stakeAmount = jsonRpcClient.tokenSwapFarmQueryTotalStake(farmAddress, tokenX, tokenY);
        return stakeAmount;
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
        return getFarmEstimatedApyAndTvlInUsd(tokenX, tokenY, liquidityTokenFarm).getItem1();
    }

    public Pair<BigDecimal, BigDecimal> getFarmEstimatedApyAndTvlInUsd(LiquidityTokenFarm liquidityTokenFarm) {
        Token tokenX = tokenService.getTokenOrElseThrow(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId(), () -> new RuntimeException("Cannot find Token by Id"));
        Token tokenY = tokenService.getTokenOrElseThrow(liquidityTokenFarm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId(), () -> new RuntimeException("Cannot find Token by Id"));
        return getFarmEstimatedApyAndTvlInUsd(tokenX, tokenY, liquidityTokenFarm);
    }

    public BigDecimal getFarmEstimatedApyByTokenIdPair(String tokenXId, String tokenYId) {
        Token tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> new RuntimeException("Cannot find Token by Id: " + tokenXId));
        Token tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> new RuntimeException("Cannot find Token by Id: " + tokenYId));
        LiquidityTokenFarm liquidityTokenFarm = liquidityTokenFarmService.findOneByTokenIdPair(tokenX.getTokenId(), tokenY.getTokenId());
        if (liquidityTokenFarm == null) {
            throw new RuntimeException("Cannot find LiquidityTokenFarm by tokenId pair: " + tokenXId + " / " + tokenYId);
        }
        return getFarmEstimatedApyAndTvlInUsd(tokenX, tokenY, liquidityTokenFarm).getItem1();
    }

    private Pair<BigDecimal, BigDecimal> getFarmEstimatedApyAndTvlInUsd(Token tokenX, Token tokenY, LiquidityTokenFarm liquidityTokenFarm) {
        BigDecimal totalTvlInUsd = getFarmTvlInUsd(tokenX, tokenY, liquidityTokenFarm);
        BigDecimal rewardPerYearInUsd = getFarmRewardPerYearInUsd(tokenX, tokenY, liquidityTokenFarm);
        int scale = 10;//Math.max(tokenXScalingFactor.toString().length(), tokenYScalingFactor.toString().length()) - 1;
        return new Pair<>(rewardPerYearInUsd
                .divide(totalTvlInUsd, scale, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)), totalTvlInUsd);
    }

    private BigDecimal getFarmRewardPerYearInUsd(Token tokenX, Token tokenY, LiquidityTokenFarm liquidityTokenFarm) {
        String farmAddress = liquidityTokenFarm.getLiquidityTokenFarmId().getFarmAddress();

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
        return rewardPerYearInUsd;
    }

    private BigDecimal getFarmTvlInUsd(Token tokenX, Token tokenY, LiquidityTokenFarm liquidityTokenFarm) {
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
        BigDecimal tokenXToUsdRate = getToUsdExchangeRateOffChainFirst(tokenX);
        BigDecimal tokenYToUsdRate = getToUsdExchangeRateOffChainFirst(tokenY);
        BigDecimal tokenXReserveInUsd = new BigDecimal(reservePair.getItem1())
                .divide(new BigDecimal(tokenXScalingFactor), tokenXScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(tokenXToUsdRate);
        BigDecimal tokenYReserveInUsd = new BigDecimal(reservePair.getItem2())
                .divide(new BigDecimal(tokenYScalingFactor), tokenYScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(tokenYToUsdRate);
        return tokenXReserveInUsd.add(tokenYReserveInUsd);
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

    private BigDecimal getToUsdExchangeRateOffChainFirst(Token token) {
        BigDecimal rate;
        try {
            rate = tokenPriceService.getToUsdExchangeRate(token.getTokenId());
        } catch (RuntimeException runtimeException) {
            LOG.info("Get token to USD price from off-chain service error.", runtimeException);
            rate = null;
        }
        if (rate == null) {
            rate = getToUsdExchangeRate(token);
        }
        return rate;
    }

    private BigDecimal getToUsdExchangeRate(Token token) {
        if (USD_EQUIVALENT_TOKEN_ID.equals(token.getTokenId())) {
            return BigDecimal.ONE;
        }
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
