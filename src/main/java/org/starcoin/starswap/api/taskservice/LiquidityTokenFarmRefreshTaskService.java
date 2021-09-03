package org.starcoin.starswap.api.taskservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.LiquidityTokenFarm;
import org.starcoin.starswap.api.data.repo.LiquidityTokenFarmRepository;
import org.starcoin.starswap.api.service.TokenService;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class LiquidityTokenFarmRefreshTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(LiquidityTokenFarmRefreshTaskService.class);
    private final JsonRpcClient jsonRpcClient;
    private final LiquidityTokenFarmRepository liquidityTokenFarmRepository;
    private final TokenService tokenService;

    public LiquidityTokenFarmRefreshTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired LiquidityTokenFarmRepository liquidityTokenFarmRepository,
            TokenService tokenService) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
        this.liquidityTokenFarmRepository = liquidityTokenFarmRepository;
        this.tokenService = tokenService;
    }

    @Scheduled(fixedDelay = 10000) //todo config
    public void task() {
        List<LiquidityTokenFarm> farms = liquidityTokenFarmRepository.findByDeactivedIsFalse();
        for (LiquidityTokenFarm farm : farms) {
            BigInteger stakeAmount = getFarmTotalStakeAmount(farm);
            farm.setTotalStakeAmount(stakeAmount);
            farm.setUpdatedAt(System.currentTimeMillis());
            farm.setUpdatedBy("admin");
            liquidityTokenFarmRepository.save(farm);
        }
    }

    private BigInteger getFarmTotalStakeAmount(LiquidityTokenFarm farm) {
        String farmAddress = farm.getLiquidityTokenFarmId().getFarmAddress();
        String tokenXId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenXId();
        String tokenX = tokenService.getTokenOrElseThrow(tokenXId, () -> { throw new RuntimeException("Cannot find token by Id: " + tokenXId);}).getTokenStructType().toTypeTagString();
        String tokenYId = farm.getLiquidityTokenFarmId().getLiquidityTokenId().getTokenYId();
        String tokenY = tokenService.getTokenOrElseThrow(tokenYId, () -> { throw new RuntimeException("Cannot find token by Id: " + tokenYId);}).getTokenStructType().toTypeTagString();
        BigInteger stakeAmount = jsonRpcClient.tokenSwapFarmQueryTotalStake(farmAddress, tokenX, tokenY);
        return stakeAmount;
    }

}
