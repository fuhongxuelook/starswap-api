package org.starcoin.starswap.api.utils;

import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.data.model.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class JsonRpcClient {

    private final JSONRPC2Session jsonRpcSession;

    private final String jsonRpcUrl;

    public JsonRpcClient(String jsonRpcUrl) throws MalformedURLException {
        this.jsonRpcUrl = jsonRpcUrl;
        this.jsonRpcSession = new JSONRPC2Session(new URL(this.jsonRpcUrl));
    }

    public String getJsonRpcUrl() {
        return jsonRpcUrl;
    }

    public Event[] getEvents(Map<String, Object> eventFilter) {
        return JsonRpcUtils.getEvents(this.jsonRpcSession, eventFilter);
    }

    public BigInteger tokenSwapFarmQueryTotalStake(String farmAddress, String tokenX, String tokenY) {
        return JsonRpcUtils.tokenSwapFarmQueryTotalStake(this.jsonRpcSession, farmAddress, tokenX, tokenY);
    }

    public Pair<BigInteger, BigInteger> getTokenSwapFarmStakedReserves(String farmAddress, String lpTokenAddress, String tokenX, String tokenY) {
        return JsonRpcUtils.getTokenSwapFarmStakedReserves(this.jsonRpcSession, farmAddress, lpTokenAddress, tokenX, tokenY);
    }

    public BigDecimal getExchangeRate(String lpTokenAddress, String tokenX, String tokenY) {
        BigInteger tokenXScalingFactor = JsonRpcUtils.tokenGetScalingFactor(jsonRpcSession, tokenX);
        BigInteger tokenYScalingFactor = JsonRpcUtils.tokenGetScalingFactor(jsonRpcSession, tokenY);
        Pair<BigInteger, BigInteger> reserves = JsonRpcUtils.tokenSwapRouterGetReserves(jsonRpcSession, lpTokenAddress, tokenX, tokenY);
        BigInteger amountX = reserves.getItem1().divide(BigInteger.valueOf(100L));//tokenXScalingFactor;
        BigInteger amountY = JsonRpcUtils.tokenSwapRouterGetAmountOut(jsonRpcSession, lpTokenAddress, amountX, reserves.getItem1(), reserves.getItem2());
        //System.out.println("----- amountX ------" + amountX);
        //System.out.println("----- amountY ------" + amountY);
        int scale = Math.max(tokenXScalingFactor.toString().length(), tokenYScalingFactor.toString().length()) - 1;
        return new BigDecimal(amountY).divide(new BigDecimal(tokenYScalingFactor), scale, RoundingMode.HALF_UP)
                .divide(new BigDecimal(amountX).divide(new BigDecimal(tokenXScalingFactor), scale, RoundingMode.HALF_UP),
                        scale, RoundingMode.HALF_UP);
    }
}
