package org.starcoin.starswap.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.starswap.api.service.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Api(tags = {"Starswap RESTful API"})
@RestController
@RequestMapping("v1/starswap")
public class StarswapController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StarswapController.class);

    @Resource
    private TokenService tokenService;

    @Resource
    private LiquidityTokenService liquidityTokenService;

    @Resource
    private LiquidityPoolService liquidityPoolService;

    @Resource
    private LiquidityAccountService liquidityAccountService;

    @Resource
    private PullingEventTaskService pullingEventTaskService;

    @Resource
    private LiquidityTokenFarmService liquidityTokenFarmService;

    @Resource
    private LiquidityTokenFarmAccountService liquidityTokenFarmAccountService;

    @Resource
    private NodeHeartbeatService nodeHeartbeatService;

    @GetMapping(path = "tokens")
    public List<Token> getTokens() {
        return tokenService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "tokens/{tokenId}")
    public Token getToken(@PathVariable(name = "tokenId") String tokenId) {
        Token token = tokenService.getToken(tokenId);
        return token;
    }

    @GetMapping(path = "liquidityTokens")
    public List<LiquidityToken> getLiquidityTokens() {
        return liquidityTokenService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "liquidityTokens/{id}")
    public LiquidityToken getLiquidityToken(@PathVariable(name = "id") @ApiParam("Token pair Id., for example 'BTC:STC'") String id) {
        String[] tokenXYId = parseTokenIdPair(id);
        return liquidityTokenService.findOneByTokenIdPair(tokenXYId[0], tokenXYId[1]);
    }

    @GetMapping(path = "liquidityPools")
    public List<LiquidityPool> getLiquidityPools() {
        return liquidityPoolService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "liquidityPools/{id}")
    public LiquidityPool getLiquidityPool(@PathVariable(name = "id") @ApiParam("Pool Id., for example 'BTC:STC'") String id) {
        String[] tokenXYId = parseTokenIdPair(id);
        return liquidityPoolService.findOneByTokenIdPair(tokenXYId[0], tokenXYId[1]);
    }

    @ApiOperation("Get LP Token farm list")
    @GetMapping(path = "lpTokenFarms")
    public List<LiquidityTokenFarm> getLiquidityTokenFarms() {
        return liquidityTokenFarmService.findByDeactivedIsFalse();
    }

    @ApiOperation("Get LP Token farm info.")
    @GetMapping(path = "lpTokenFarms/{id}")
    public LiquidityTokenFarm getLiquidityTokenFarms(@PathVariable(name = "id") @ApiParam("Farm Id., for example 'BTC:STC'") String id) {
        String[] tokenXYId = parseTokenIdPair(id);
        return liquidityTokenFarmService.findOneByTokenIdPair(tokenXYId[0], tokenXYId[1]);
    }

    @GetMapping(path = "liquidityAccounts")
    public List<LiquidityAccount> getLiquidityAccounts(
            @RequestParam(value = "accountAddress", required = true) String accountAddress) {
        return liquidityAccountService.findByAccountAddress(accountAddress);
    }

    @GetMapping(path = "lpTokenFarmAccounts")
    public List<LiquidityTokenFarmAccount> getFarmAccounts(
            @RequestParam(value = "accountAddress", required = true) String accountAddress) {
        return liquidityTokenFarmAccountService.findByAccountAddress(accountAddress);
    }

    @PostMapping(path = "pullingEventTasks")
    public void postPullingEventTask(@RequestBody PullingEventTask pullingEventTask) {
        pullingEventTaskService.createPullingEventTask(pullingEventTask);
    }

    @GetMapping(path = "heartbeatBreakIntervals")
    public List<Pair<BigInteger, BigInteger>> getBreakIntervals() {
        return nodeHeartbeatService.findBreakIntervals();
    }

    @ApiOperation("Get Total Valued Locked in all farms")
    @GetMapping(path = "farmingTvlInUsd")
    public BigDecimal getFarmingTvlInUsd() {
        return liquidityTokenFarmService.getTotalValueLockedInUsd();
    }

    private String[] parseTokenIdPair(String tokenPairId) {
        String[] xy = tokenPairId.split(":");
        if (xy.length < 2) throw new IllegalArgumentException();
        TokenIdPair tokenIdPair = new TokenIdPair(xy[0], xy[1]);
        return tokenIdPair.toStringArray();
    }


}
