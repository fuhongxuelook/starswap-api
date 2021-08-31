package org.starcoin.starswap.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.starswap.api.service.*;

import javax.annotation.Resource;
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
    public LiquidityToken getLiquidityToken(@PathVariable(name = "id") String id) {
        String[] tokenXYId = parseTokenIdPair(id);
        return liquidityTokenService.findOneByTokenIdPair(tokenXYId[0], tokenXYId[1]);
    }

    @GetMapping(path = "liquidityPools")
    public List<LiquidityPool> getLiquidityPools() {
        return liquidityPoolService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "liquidityPools/{id}")
    public LiquidityPool getLiquidityPool(@PathVariable(name = "id") String id) {
        //String[] axy = poolId.split("::");
        //if (axy.length < 2) throw new IllegalArgumentException();
        String[] tokenXYId = parseTokenIdPair(id);
        //LiquidityPoolId liquidityPoolIdObj = new LiquidityPoolId(liquidityTokenIdObj, axy[0]);
        return liquidityPoolService.findOneByTokenIdPair(tokenXYId[0], tokenXYId[1]);
    }

    @GetMapping(path = "liquidityAccounts")
    public List<LiquidityAccount> getLiquidityAccounts(
            @RequestParam(value = "accountAddress", required = true) String accountAddress) {
        return liquidityAccountService.findByAccountAddress(accountAddress);
    }

    @PostMapping(path = "pullingEventTasks")
    public void postPullingEventTask(@RequestBody PullingEventTask pullingEventTask) {
        pullingEventTaskService.createPullingEventTask(pullingEventTask);
    }

    private String[] parseTokenIdPair(String tokenPairId) {
        String[] xy = tokenPairId.split(":");
        if (xy.length < 2) throw new IllegalArgumentException();
        return xy;
        //LiquidityTokenId liquidityTokenIdObj = new LiquidityTokenId(xy[0], xy[1]);
    }


}
