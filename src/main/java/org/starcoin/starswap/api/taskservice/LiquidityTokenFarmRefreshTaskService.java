package org.starcoin.starswap.api.taskservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityTokenFarm;
import org.starcoin.starswap.api.data.model.Pair;
import org.starcoin.starswap.api.data.repo.LiquidityTokenFarmRepository;
import org.starcoin.starswap.api.service.OnChainService;
import org.starcoin.starswap.api.service.TokenService;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class LiquidityTokenFarmRefreshTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(LiquidityTokenFarmRefreshTaskService.class);
    private final JsonRpcClient jsonRpcClient;
    private final LiquidityTokenFarmRepository liquidityTokenFarmRepository;
    private final TokenService tokenService;
    private final OnChainService onChainService;

    public LiquidityTokenFarmRefreshTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired LiquidityTokenFarmRepository liquidityTokenFarmRepository,
            @Autowired TokenService tokenService,
            @Autowired OnChainService onChainService) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
        this.liquidityTokenFarmRepository = liquidityTokenFarmRepository;
        this.tokenService = tokenService;
        this.onChainService = onChainService;
    }

    @Scheduled(fixedDelay = 10000) //todo config
    public void task() {
        List<LiquidityTokenFarm> farms = liquidityTokenFarmRepository.findByDeactivedIsFalse();
        for (LiquidityTokenFarm farm : farms) {
            farm.setTotalStakeAmount(onChainService.getFarmTotalStakeAmount(farm));
            Pair<BigDecimal, BigDecimal> estimatedApyAndTvlInUsd = onChainService.getFarmEstimatedApyAndTvlInUsd(farm);
            farm.setEstimatedApy(estimatedApyAndTvlInUsd.getItem1());
            farm.setTvlInUsd(estimatedApyAndTvlInUsd.getItem2());
            farm.setUpdatedAt(System.currentTimeMillis());
            farm.setUpdatedBy("admin");
            liquidityTokenFarmRepository.save(farm);
        }
    }


}
