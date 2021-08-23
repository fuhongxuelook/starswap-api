package org.starcoin.starswap.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.starswap.api.bean.*;
import org.starcoin.starswap.api.service.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = {"Starswap RESTful API"})
@RestController
@RequestMapping("v1/starswap")
public class StarswapController {

    private static final Logger logger = LoggerFactory.getLogger(StarswapController.class);

    @Resource
    private TokenService tokenService;

    @Resource
    private TokenPairService tokenPairService;

    @Resource
    private TokenPairPoolService tokenPairPoolService;

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

    @GetMapping(path = "tokenPairs")
    public List<TokenPair> getTokenPairs() {
        return tokenPairService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "tokenPairs/{tokenPairId}")
    public TokenPair getTokenPair(@PathVariable(name = "tokenPairId") String tokenPairId) {
        TokenPairId tokenPairIdObj = parseTokenPairId(tokenPairId);
        return tokenPairService.getTokenPair(tokenPairIdObj);
    }

    private TokenPairId parseTokenPairId(String tokenPairId) {
        String[] xy = tokenPairId.split(":");
        if (xy.length < 2) throw new IllegalArgumentException();
        TokenPairId tokenPairIdObj = new TokenPairId(xy[0], xy[1]);
        return tokenPairIdObj;
    }

    @GetMapping(path = "tokenPairPools")
    public List<TokenPairPool> getTokenPairPools() {
        return tokenPairPoolService.findByDeactivedIsFalse();
    }

    @GetMapping(path = "tokenPairPools/{tokenPairPoolId}")
    public TokenPairPool getTokenPairPool(@PathVariable(name = "tokenPairPoolId") String tokenPairPoolId) {
        String[] axy = tokenPairPoolId.split("::");
        if (axy.length < 2) throw new IllegalArgumentException();
        TokenPairId tokenPairIdObj = parseTokenPairId(axy[1]);
        TokenPairPoolId tokenPairPoolIdObj = new TokenPairPoolId(tokenPairIdObj, axy[0]);
        return tokenPairPoolService.getTokenPairPool(tokenPairPoolIdObj);
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


}
