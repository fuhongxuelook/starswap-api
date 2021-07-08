package org.starcoin.starswap.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.novi.serde.DeserializationError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.starswap.api.service.ContractService;
import org.starcoin.starswap.api.service.TokenService;
//import org.starcoin.starswap.api.service.TransactionService;
import org.starcoin.starswap.api.vo.Result;
import org.starcoin.starswap.api.vo.ResultUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Api(tags = {"投票列表配置接口"}, description = "投票列表配置接口，包含管理服务API")
@RestController
@RequestMapping("v1/starswap")
public class StarswapController {

    private static final Logger logger = LoggerFactory.getLogger(StarswapController.class);

    @Resource
    private TokenService tokenService;

    @Resource
    private ContractService contractService;


//    @Resource
//    private TransactionService transactionService;
//

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
