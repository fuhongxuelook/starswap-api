package org.starcoin.starswap.subscribe.handler;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starcoin.bean.Event;
import org.starcoin.bean.EventNotification;
import org.starcoin.starswap.api.service.HandleEventService;
import org.starcoin.starswap.subscribe.StarcoinSubscriber;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;

public class EventsSubscribeHandler implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(EventsSubscribeHandler.class);

    private String host;

    private String network;

    //private ElasticSearchHandler elasticSearchHandler;
    private HandleEventService handleEventService;

    public EventsSubscribeHandler(String host, String network,
                                  //LiquidityAccountService liquidityAccountService, TokenService tokenService
                                  //, ElasticSearchHandler elasticSearchHandler
                                  HandleEventService handleEventService
    ) {
        this.host = host;
        this.network = network;
        this.handleEventService = handleEventService;
    }

    @Override
    public void run() {
        try {
            WebSocketService service = new WebSocketService("ws://" + host + ":9870", true);
            service.connect();
            StarcoinSubscriber subscriber = new StarcoinSubscriber(service);
            Flowable<EventNotification> flowableEvents = subscriber.newEventNotifications();
            //TransactionRPCClient rpc = new TransactionRPCClient(new URL("http://" + host + ":9850"));

            for (EventNotification notification : flowableEvents.blockingIterable()) {
                //PendingTransaction transaction = rpc.getTransaction(notification);
                //elasticSearchHandler.saveTransaction(network, transaction);
                //System.out.println(notification);
                //System.out.println(notification.getParams().getResult());
                if (notification.getParams() == null || notification.getParams().getResult() == null) {
                    continue;
                }
                Event event = notification.getParams().getResult();
                LOG.debug("Received event: " + event);
                handleEventService.handleEvent(event);
            }
        } catch (ConnectException e) {// | MalformedURLException | JSONRPC2SessionException e) {
            LOG.info("handle subscribe exception", e);
        }
    }
}
