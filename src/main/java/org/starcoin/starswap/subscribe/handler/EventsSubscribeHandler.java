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

    private String webSocketSeed;

    private String network;

    private HandleEventService handleEventService;

    public EventsSubscribeHandler(String seed, String network,
                                  //LiquidityAccountService liquidityAccountService, TokenService tokenService
                                  //, ElasticSearchHandler elasticSearchHandler
                                  HandleEventService handleEventService
    ) {
        this.webSocketSeed = seed;
        this.network = network;
        this.handleEventService = handleEventService;
    }

    private String getWebSocketSeed() {
        String wsUrl = webSocketSeed;
        String wsPrefix = "ws://";
        if (!wsUrl.startsWith(wsPrefix)) {
            wsUrl = wsPrefix + wsUrl;
        }
        if (wsUrl.lastIndexOf(":") == wsUrl.indexOf(":")) {
            wsUrl = wsUrl + ":9870";
        }
        LOG.debug("Get WebSocket URL: " + wsUrl);
        return wsUrl;
    }

    @Override
    public void run() {
        try {
            WebSocketService service = new WebSocketService(getWebSocketSeed(), true);
            service.connect();
            StarcoinSubscriber subscriber = new StarcoinSubscriber(service);
            Flowable<EventNotification> flowableEvents = subscriber.newEventNotifications();

            for (EventNotification notification : flowableEvents.blockingIterable()) {
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
