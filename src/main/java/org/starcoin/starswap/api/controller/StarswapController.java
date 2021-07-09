package org.starcoin.starswap.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starcoin.starswap.api.bean.*;
import org.starcoin.starswap.api.service.ContractService;
import org.starcoin.starswap.api.service.TokenPairPoolService;
import org.starcoin.starswap.api.service.TokenPairService;
import org.starcoin.starswap.api.service.TokenService;

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
    private ContractService contractService;


//    @Resource
//    private TransactionService transactionService;

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

//
//    @ApiOperation(value = "获取指定poll的详情列表数据")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "network", value = "网络参数", required = true, dataType = "String", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "proposalId", value = "proposalId", required = true, dataType = "Long", dataTypeClass = Long.class),
//            @ApiImplicitParam(name = "proposer", value = "proposer", required = true, dataType = "String", dataTypeClass = String.class)
//    })
//    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
//    @GetMapping("/votes/{network}/{proposalId}/{proposer}")
//    public Result<List<JSONObject>> delPollItem(@PathVariable("network") String network,
//                                                @PathVariable("proposalId") Long proposalId,
//                                                @PathVariable("proposer") String proposer) throws IOException, DeserializationError {
//        List<JSONObject> list = transactionService.getEventsByProposalIdAndProposer(network, proposalId, proposer);
//        return ResultUtils.success(list);
//    }
}
