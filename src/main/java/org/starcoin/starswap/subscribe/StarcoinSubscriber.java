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

public class StarcoinSubscriber {

    private final Web3jService web3jService;

    public StarcoinSubscriber(Web3jService web3jService) {
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


    public Flowable<EventNotification> newEventNotifications() {
        Map<String, Object> eventFilter = new HashMap<>();
//        eventFilter.put("from_block", 100);
//        eventFilter.put("to_block", 3);
        return web3jService.subscribe(
                new Request<>(
                        "starcoin_subscribe",
                        Arrays.asList(Kind.Events, eventFilter),
                        web3jService,
                        EthSubscribe.class),
                "starcoin_unsubscribe",
                EventNotification.class);
    }



}
