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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OnChainService {

    private static final Logger LOG = LoggerFactory.getLogger(OnChainService.class);

    private final JsonRpcClient jsonRpcClient;

    @Value("${starswap.usd-equivalent-token-id}")
    private String usdEquivalentTokenId;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LiquidityTokenService liquidityTokenService;

    @Autowired
    private LiquidityTokenFarmService liquidityTokenFarmService;

    /**
     * Off-chain token price service.
     */
    @Autowired
    private TokenPriceService tokenPriceService;

    public OnChainService(@Value("${starcoin.json-rpc-url}") String jsonRpcUrl) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
    }

    public List<String> getBestSwapPath(String tokenXId, String tokenYId, BigInteger amountX) {
        String tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> {
            throw new RuntimeException("Cannot find token by Id: " + tokenXId);
        }).getTokenStructType().toTypeTagString();
        String tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> {
            throw new RuntimeException("Cannot find token by Id: " + tokenYId);
        }).getTokenStructType().toTypeTagString();
        LiquidityToken liquidityToken = liquidityTokenService.findOneByTokenIdPair(tokenXId, tokenYId);
        List<String> indirectSwapPath = liquidityTokenService.getShortestIndirectSwapPath(tokenXId, tokenYId);
        BigInteger directAmountY = null;
        if (liquidityToken != null) {
            if (indirectSwapPath.size() == 0 || indirectSwapPath.size() > 3) {
                return Arrays.asList(tokenXId, tokenYId);
            }
            // todo call On-Chain contract twice??
            directAmountY = jsonRpcClient.tokenSwapRouterGetAmountOut(liquidityToken.getLiquidityTokenId().getLiquidityTokenAddress(), tokenX, tokenY, amountX);
        }
        if (liquidityToken == null && (indirectSwapPath.size() == 0 || indirectSwapPath.size() > 3)) {
            return Collections.emptyList();
        }
        if (liquidityToken == null) {
            return indirectSwapPath;
        }
        String tokenRId = indirectSwapPath.get(1);
        String tokenR = tokenService.getTokenOrElseThrow(tokenRId, () -> {
            throw new RuntimeException("Cannot find token by Id: " + tokenRId);
        }).getTokenStructType().toTypeTagString();
        LiquidityToken liquidityTokenXR = liquidityTokenService.findOneByTokenIdPair(tokenXId, tokenRId);
        // todo call On-Chain contract twice??
        BigInteger amountR = jsonRpcClient.tokenSwapRouterGetAmountOut(liquidityTokenXR.getLiquidityTokenId().getLiquidityTokenAddress(), tokenX, tokenR, amountX);
        LiquidityToken liquidityTokenRY = liquidityTokenService.findOneByTokenIdPair(tokenRId, tokenYId);
        // todo call On-Chain contract twice??
        BigInteger indirectAmountY = jsonRpcClient.tokenSwapRouterGetAmountOut(liquidityTokenRY.getLiquidityTokenId().getLiquidityTokenAddress(), tokenR, tokenY, amountR);
        return directAmountY.compareTo(indirectAmountY) > 0 ? Arrays.asList(tokenXId, tokenYId) : indirectSwapPath;
    }

    public BigInteger getFarmTotalStakeAmount(LiquidityTokenFarm farm) {
        String farmAddress = farm.getLiquidityTokenFarmId().getFarmAddress();
        String tokenXId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId();
        String tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> {
            throw new RuntimeException("Cannot find token by Id: " + tokenXId);
        }).getTokenStructType().toTypeTagString();
        String tokenYId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId();
        String tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> {
            throw new RuntimeException("Cannot find token by Id: " + tokenYId);
        }).getTokenStructType().toTypeTagString();
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
        BigInteger rewardTokenScalingFactor = getTokenScalingFactorOffChainFirst(rewardToken);
        BigDecimal rewardTokenToUsdRate = getToUsdExchangeRate(rewardToken);
        BigDecimal rewardPerYearInUsd = new BigDecimal(rewardPerYear)
                .divide(new BigDecimal(rewardTokenScalingFactor), rewardTokenScalingFactor.toString().length() - 1, RoundingMode.HALF_UP)
                .multiply(rewardTokenToUsdRate);
        return rewardPerYearInUsd;
    }

    /**
     * get farm total value locked in USD.
     */
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
        BigInteger tokenXScalingFactor = getTokenScalingFactorOffChainFirst(tokenX);
        BigInteger tokenYScalingFactor = getTokenScalingFactorOffChainFirst(tokenY);
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

    // get token scaling factor from database first, or else get from on-chain.
    private BigInteger getTokenScalingFactorOffChainFirst(Token token) {
        if (token.getScalingFactor() != null) {
            return token.getScalingFactor();
        }
        return jsonRpcClient.tokenGetScalingFactor(token.getTokenStructType().toTypeTagString());
    }

    public BigInteger getTokenScalingFactor(String typeTag) {
        return jsonRpcClient.tokenGetScalingFactor(typeTag);
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
        if (usdEquivalentTokenId.equals(token.getTokenId())) {
            return BigDecimal.ONE;
        }
        Token usdEquivalentToken = tokenService.getTokenOrElseThrow(usdEquivalentTokenId, () -> new RuntimeException("Cannot find USD equivalent token."));
        LiquidityToken liquidityToken = liquidityTokenService.findOneByTokenIdPair(token.getTokenId(), usdEquivalentToken.getTokenId());
        if (liquidityToken == null) {
            throw new RuntimeException("Cannot find LiquidityToken by tokenId pair: " + token.getTokenId() + " / " + usdEquivalentToken.getTokenId());
        }
        //String usdEquivalentTokenTypeTag = usdEquivalentToken.getTokenStructType().toTypeTagString();
        //return jsonRpcClient.getExchangeRate(liquidityToken.getLiquidityTokenId().getLiquidityTokenAddress(),
        //        token.getTokenStructType().toTypeTagString(), usdEquivalentTokenTypeTag);
        return jsonRpcClient.getExchangeRate(liquidityToken.getLiquidityTokenId().getLiquidityTokenAddress(),
                token.getTokenStructType().toTypeTagString(), usdEquivalentToken.getTokenStructType().toTypeTagString(),
                getTokenScalingFactorOffChainFirst(token), getTokenScalingFactorOffChainFirst(usdEquivalentToken));
    }

    /**
     * refresh token scaling factors in database.
     */
    public void refreshOffChainScalingFactors() {
        tokenService.findByScalingFactorIsNull().forEach((t) -> {
            if (t.getScalingFactor() == null) {
                t.setScalingFactor(jsonRpcClient.tokenGetScalingFactor(t.getTokenStructType().toTypeTagString()));
                t.setUpdatedAt(System.currentTimeMillis());
                t.setUpdatedBy("admin");
                tokenService.save(t);
            }
        });
    }
}
