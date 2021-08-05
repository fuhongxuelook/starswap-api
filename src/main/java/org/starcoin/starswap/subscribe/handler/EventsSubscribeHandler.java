package org.starcoin.starswap.subscribe.handler;

import com.novi.serde.DeserializationError;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starcoin.bean.Event;
import org.starcoin.bean.EventNotification;
import org.starcoin.starswap.api.bean.LiquidityAccountId;
import org.starcoin.starswap.api.bean.Token;
import org.starcoin.starswap.api.bean.TokenPairId;
import org.starcoin.starswap.api.bean.TokenPairPoolId;
import org.starcoin.starswap.api.service.LiquidityAccountService;
import org.starcoin.starswap.api.service.TokenService;
import org.starcoin.starswap.subscribe.StarcoinSubscriber;
import org.starcoin.starswap.types.AddLiquidityEvent;
import org.starcoin.utils.CommonUtils;
import org.web3j.protocol.websocket.WebSocketService;

import java.math.BigInteger;
import java.net.ConnectException;
import java.util.List;

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
                StructTag structTag = tryParseStructTag(event.getTypeTag());
                if (structTag == null) {
                    continue;
                }
                if (!"AddLiquidityEvent".equals(structTag.getName())) {
                    continue;
                }
                // 先假设事件的结构的地址就是池子的地址……
                String tokenPairPoolAddress = structTag.getAddress();
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

    private StructTag tryParseStructTag(String s) {
        // TypeTag example:
        // 0x00000000000000000000000000000001::Oracle::OracleUpdateEvent<0x07fa08a855753f0ff7292fdcbe871216::YFI_USD::YFI_USD, u128>
        String[] fs = s.split("::", 3);
        if (fs.length != 3) {
            return null;
        }
        StructTag t = new StructTag();
        t.setAddress(fs[0]);
        t.setModule(fs[1]);
        int idxOfLT = fs[2].indexOf("<");
        if (idxOfLT == -1) {
            t.setName(fs[2]);
        } else {
            t.setName(fs[2].substring(0, idxOfLT));
            //System.out.println("----------- parsed struct tag: " + t);
            //todo parse type params???
        }
        return t;
    }

    public static class StructTag {
        /*
              "type_tag": {
              "Struct": {
                "address": "0x00000000000000000000000000000001",
                "module": "Account",
                "name": "DepositEvent",
                "type_params": []
              }
            },
         */

        String address;
        String module;
        String name;

        //@JsonProperty("type_params")
        List<Object> typeParams; // Is ok???

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Object> getTypeParams() {
            return typeParams;
        }

        public void setTypeParams(List<Object> typeParams) {
            this.typeParams = typeParams;
        }

        @Override
        public String toString() {
            return "StructTag{" +
                    "address='" + address + '\'' +
                    ", module='" + module + '\'' +
                    ", name='" + name + '\'' +
                    ", typeParams=" + typeParams +
                    '}';
        }
    }

}
