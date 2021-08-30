package org.starcoin.starswap.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.service.LiquidityPoolService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import static org.starcoin.starswap.subscribe.StarcoinEventSubscriber.createEventFilterMap;

public class JsonRpcUtils {

    private static final Logger LOG = LoggerFactory.getLogger(LiquidityPoolService.class);


    public static Event[] getEvents(JSONRPC2Session jsonRpcSession, BigInteger fromBlockNumber, BigInteger toBlockNumber) {
        String method = "chain.get_events";
        Map<String, Object> eventFilter = createEventFilterMap();
        eventFilter.put("from_block", fromBlockNumber);
        eventFilter.put("to_block", toBlockNumber);
        JSONRPC2Request request = new JSONRPC2Request(method, Arrays.asList(eventFilter), System.currentTimeMillis());
        JSONRPC2Response response = null;
        try {
            response = jsonRpcSession.send(request);
        } catch (JSONRPC2SessionException e) {
            LOG.error("JSON rpc error.", e);
            return null;
        }
        Event[] events = null;
        if (response.indicatesSuccess()) {
            Object result = response.getResult();
            if (result != null) {
                try {
                    events = getObjectMapper().readValue(result.toString(), Event[].class);
                } catch (JsonProcessingException e) {
                    LOG.error("JSON RPC parsing result error.", e);
                    throw new RuntimeException("JSON RPC parsing result error.", e);
                }
            }
        } else {
            LOG.error("JSON RPC chain.get_events error.");
            throw new RuntimeException("JSON RPC chain.get_events error.");
        }
        return events == null ? new Event[0] : events;
    }

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


}
