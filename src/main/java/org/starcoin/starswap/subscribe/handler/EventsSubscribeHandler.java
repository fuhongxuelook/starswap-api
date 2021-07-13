package org.starcoin.starswap.subscribe.handler;

import com.novi.serde.DeserializationError;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starcoin.base.AddLiquidityEvent;
import org.starcoin.starswap.api.bean.LiquidityAccountId;
import org.starcoin.starswap.api.bean.Token;
import org.starcoin.starswap.api.bean.TokenPairId;
import org.starcoin.starswap.api.bean.TokenPairPoolId;
import org.starcoin.starswap.api.service.LiquidityAccountService;
import org.starcoin.starswap.api.service.TokenService;
import org.starcoin.starswap.subscribe.StarcoinSubscriber;
import org.starcoin.bean.EventNotification;
import org.starcoin.utils.CommonUtils;
import org.web3j.protocol.websocket.WebSocketService;
import org.starcoin.bean.Event;

import java.math.BigInteger;
import java.net.ConnectException;

public class EventsSubscribeHandler implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(EventsSubscribeHandler.class);

    private String host;

    private String network;

    private LiquidityAccountService liquidityAccountService;

    private TokenService tokenService;

    //private ElasticSearchHandler elasticSearchHandler;

    public EventsSubscribeHandler(String host, String network,
                                  LiquidityAccountService liquidityAccountService, TokenService tokenService) { //, ElasticSearchHandler elasticSearchHandler) {
        this.host = host;
        this.network = network;
        this.liquidityAccountService = liquidityAccountService;
        this.tokenService = tokenService;
        //this.elasticSearchHandler = elasticSearchHandler;
    }

    @Override
    public void run() {
        try {
            WebSocketService service = new WebSocketService("ws://" + host + ":9870", true);
            service.connect();
            StarcoinSubscriber subscriber = new StarcoinSubscriber(service);
            Flowable<EventNotification> flowableEvents = subscriber.newEventsNotifications();
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
                //System.out.println(event);
                // todo 就这样过滤事件？
                if (!"AddLiquidityEvent".equals(event.getTypeTag().getStruct().getName())) {
                    continue;
                }
                // 先假设事件的结构的地址就是池子的地址……
                String tokenPairPoolAddress = event.getTypeTag().getStruct().getAddress();
                //System.out.println(tokenPairPoolAddress);
                String eventData = event.getData();
                AddLiquidityEvent addLiquidityEvent = null;
                try {
                    addLiquidityEvent = AddLiquidityEvent.bcsDeserialize(CommonUtils.hexToByteArray(eventData));
                } catch (DeserializationError deserializationError) {
                    //deserializationError.printStackTrace();
                    LOG.error("AddLiquidityEvent.bcsDeserialize error.", deserializationError);
                    continue;
                }
                //System.out.println(addLiquidityEvent);
                String xTokenTypeAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.x_token_code.address.value);
                String xTokenTypeModule = addLiquidityEvent.x_token_code.module;
                String xTokenTypeName = addLiquidityEvent.x_token_code.name;
                String yTokenTypeAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.y_token_code.address.value);
                String yTokenTypeModule = addLiquidityEvent.y_token_code.module;
                String yTokenTypeName = addLiquidityEvent.y_token_code.name;
                String accountAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.signer.value);
                BigInteger liquidity = addLiquidityEvent.liquidity;
                Token xToken = tokenService.getTokenByStructType(xTokenTypeAddress, xTokenTypeModule, xTokenTypeName);
                if (xToken == null) {
                    LOG.info("Cannot get token by struct type.");
                    continue;
                }
                Token yToken = tokenService.getTokenByStructType(yTokenTypeAddress, yTokenTypeModule, yTokenTypeName);
                if (yToken == null) {
                    LOG.info("Cannot get token by struct type.");
                    continue;
                }
                LiquidityAccountId liquidityAccountId = new LiquidityAccountId(accountAddress,
                        new TokenPairPoolId(new TokenPairId(xToken.getTokenId(), yToken.getTokenId()), tokenPairPoolAddress));
                this.liquidityAccountService.activeLiquidityAccount(liquidityAccountId);
                // todo ???
            }
        } catch (ConnectException e) {// | MalformedURLException | JSONRPC2SessionException e) {
            LOG.info("handle subscribe exception", e);
        }
    }
}
