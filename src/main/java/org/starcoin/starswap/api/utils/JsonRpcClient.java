package org.starcoin.starswap.api.utils;

import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import org.starcoin.bean.Event;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

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

    public Event[] getEvents(BigInteger fromBlockNumber, BigInteger toBlockNumber) {
        return JsonRpcUtils.getEvents(this.jsonRpcSession, fromBlockNumber, toBlockNumber);
    }

    public BigInteger tokenSwapFarmQueryTotalStake(String farmAddress, String tokenX, String tokenY) {
        return JsonRpcUtils.tokenSwapFarmQueryTotalStake(this.jsonRpcSession, farmAddress, tokenX, tokenY);
    }
}
