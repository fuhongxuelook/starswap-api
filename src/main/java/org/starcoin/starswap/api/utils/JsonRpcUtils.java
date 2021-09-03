package org.starcoin.starswap.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.*;
import java.util.function.Function;

import static org.starcoin.starswap.subscribe.StarcoinEventSubscriber.createEventFilterMap;

public class JsonRpcUtils {

    private static final Logger LOG = LoggerFactory.getLogger(LiquidityPoolService.class);


    public static Event[] getEvents(JSONRPC2Session jsonRpcSession, BigInteger fromBlockNumber, BigInteger toBlockNumber) {
        String method = "chain.get_events";
        Map<String, Object> eventFilter = createEventFilterMap();
        eventFilter.put("from_block", fromBlockNumber);
        eventFilter.put("to_block", toBlockNumber);
        Class<Event[]> objectClass = Event[].class;
        Event[] events = callForObject(jsonRpcSession, method, Collections.singletonList(eventFilter), objectClass);
        return events == null ? new Event[0] : events;
    }


    //public fun query_total_stake<TokenX: store, TokenY: store>() : u128
    public static BigInteger tokenSwapFarmQueryTotalStake(JSONRPC2Session jsonRpcSession, String farmAddress, String tokenX, String tokenY) {
        List<String> resultFields = contractCallV2(jsonRpcSession, farmAddress + "::TokenSwapFarmScript::query_total_stake",
                Arrays.asList(tokenX, tokenY), Collections.emptyList(), new TypeReference<List<String>>() {
                });
        return new BigInteger(resultFields.get(0)); //todo Is this ok? need test...
    }

    /**
     * JSON RPC call method 'contract.call_v2'.
     *
     * @param functionId function Id.
     * @param typeArgs   type arguments.
     * @param args       arguments.
     * @return JSON RPC response body.
     */
    public static String contractCallV2(JSONRPC2Session jsonRpcSession, String functionId, List<String> typeArgs, List<Object> args) {
        String method = "contract.call_v2";
        List<Object> params = getContractCallV2Params(functionId, typeArgs, args);
        return callForObject(jsonRpcSession, method, params, String.class);
    }

    public static <T> T contractCallV2(JSONRPC2Session jsonRpcSession, String functionId, List<String> typeArgs, List<Object> args, TypeReference<T> typeReference) {
        String method = "contract.call_v2";
        List<Object> params = getContractCallV2Params(functionId, typeArgs, args);
        return callForObject(jsonRpcSession, method, params, typeReference);
    }

    private static List<Object> getContractCallV2Params(String functionId, List<String> typeArgs, List<Object> args) {
        Map<String, Object> singleParamMap = new HashMap<>();
        singleParamMap.put("function_id", functionId);
        singleParamMap.put("type_args", typeArgs);
        singleParamMap.put("args", args);
        List<Object> params = Collections.singletonList(singleParamMap);
        return params;
    }

    private static <T> T callForObject(JSONRPC2Session jsonRpcSession, String method, List<Object> params, TypeReference<T> typeRef) {
        return callForObject(jsonRpcSession, method, params, (result) -> {
            try {
                return getObjectMapper().readValue(result.toString(), typeRef);
            } catch (JsonProcessingException e) {
                LOG.error("JSON RPC parsing result error.", e);
                throw new RuntimeException("JSON RPC parsing result error.", e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T callForObject(JSONRPC2Session jsonRpcSession, String method, List<Object> params, Class<T> objectClass) {
        return callForObject(jsonRpcSession, method, params, (result) -> {
            try {
                if (objectClass.isAssignableFrom(result.getClass())) {
                    return (T) result;
                }
                if (objectClass.isAssignableFrom(String.class)) {
                    return (T) result.toString();
                }
                return getObjectMapper().readValue(result.toString(), objectClass);
            } catch (JsonProcessingException e) {
                LOG.error("JSON RPC parsing result error.", e);
                throw new RuntimeException("JSON RPC parsing result error.", e);
            }
        });
    }

    private static <T> T callForObject(JSONRPC2Session jsonRpcSession,
                                       String method, List<Object> params,
                                       Function<Object, T> resultConverter) {
        JSONRPC2Request request = new JSONRPC2Request(method, params, System.currentTimeMillis());
        JSONRPC2Response response;
        try {
            response = jsonRpcSession.send(request);
        } catch (JSONRPC2SessionException e) {
            LOG.error("JSON rpc error.", e);
            throw new RuntimeException(e);
        }
        if (response.indicatesSuccess()) {
            Object result = response.getResult();
            if (result != null) {
                return resultConverter.apply(result);
            } else {
                return null;
            }
        } else {
            LOG.error("JSON RPC call error: " + response.getError());
            throw new RuntimeException("JSON RPC call error: " + response.getError());
        }
    }

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


}
