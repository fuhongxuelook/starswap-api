package org.starcoin.starswap.subscribe;

import io.reactivex.Flowable;
import org.starcoin.bean.EventNotification;
import org.starcoin.bean.Kind;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSubscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StarcoinEventSubscriber {

    private final Web3jService web3jService;

    public StarcoinEventSubscriber(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

//    public Flowable<PendingTransactionNotification> newPendingTransactionsNotifications() {
//        return web3jService.subscribe(
//                new Request<>(
//                        "starcoin_subscribe",
//                        Arrays.asList(Kind.PendingTxn),
//                        web3jService,
//                        EthSubscribe.class),
//                "starcoin_unsubscribe",
//                PendingTransactionNotification.class);
//    }

    public static final String FROM_ADDRESS = "0xcCF1ADEdf0Ba6f9BdB9A6905173A5d72";

    public static final String ADD_LIQUIDITY_EVENT_TYPE_TAG = "xxxxxxxxx::xxx::AddLiquidityEvent";


    public Flowable<EventNotification> eventNotificationFlowable() {
        Map<String, Object> eventFilter = createEventFilterMap();
        return web3jService.subscribe(
                new Request<>(
                        "starcoin_subscribe",
                        Arrays.asList(Kind.Events, eventFilter),
                        web3jService,
                        EthSubscribe.class),
                "starcoin_unsubscribe",
                EventNotification.class);
    }

    public static Map<String, Object> createEventFilterMap() {
        Map<String, Object> eventFilter = new HashMap<>();
        //eventFilter.put("addr", FROM_ADDRESS);
        //eventFilter.put("type_tags", Collections.singletonList(ADD_LIQUIDITY_EVENT_TYPE_TAG));
        //eventFilter.put("decode", false);
        return eventFilter;
    }


}
