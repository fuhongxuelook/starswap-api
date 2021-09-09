package org.starcoin.starswap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenPriceService {

    @Value("${starswap.token-price-service.get-pair-price-url}")
    private String getPairPriceUrl;

    //@Value("${starswap.token-price-service.to-usd-pair-id-mappings}") //todo config?
    private Map<String, String> toUsdPairIdMappings;

    @Autowired
    private RestTemplate restTemplate;

    public TokenPriceService() {
        toUsdPairIdMappings = new HashMap<>();
        toUsdPairIdMappings.put("STC", "STCUSD");
    }

    public BigDecimal getToUsdExchangeRate(String tokenId) {
        String pairId = toUsdPairIdMappings.containsKey(tokenId) ? toUsdPairIdMappings.get(tokenId) : tokenId + "_USD";
        Map<String, Object> priceInfo = restTemplate.getForObject(getPairPriceUrl.replace("{pairId}", pairId), Map.class);
        if (priceInfo == null) {
            return null;
        }
        BigInteger latestPrice = new BigInteger(priceInfo.get("latestPrice").toString());
        int decimals = Integer.parseInt(priceInfo.get("decimals").toString());
        return new BigDecimal(latestPrice).divide(BigDecimal.TEN.pow(decimals),10, RoundingMode.HALF_UP);
    }
}
