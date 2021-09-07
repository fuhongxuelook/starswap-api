package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.utils.CommonUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class HandleEventService {

    private static final Logger LOG = LoggerFactory.getLogger(HandleEventService.class);

    private final LiquidityAccountService liquidityAccountService;

    private final TokenService tokenService;

    public HandleEventService(@Autowired LiquidityAccountService liquidityAccountService,
                              @Autowired TokenService tokenService) {
        this.liquidityAccountService = liquidityAccountService;
        this.tokenService = tokenService;
    }

    private static TypeTagStruct tryParseTypeTagStruct(String s) {
        // TypeTag example:
        // 0x00000000000000000000000000000001::Oracle::OracleUpdateEvent<0x07fa08a855753f0ff7292fdcbe871216::YFI_USD::YFI_USD, u128>
        String[] fs = s.split("::", 3);
        if (fs.length != 3) {
            return null;
        }
        TypeTagStruct t = new TypeTagStruct();
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

    // token_code={addr=0x07fa08a855753f0ff7292fdcbe871216, module_name=0x426f74, name=0x426f74}
    public static StructType tokenCodeMapToStructType(Map<String, Object> m) {
        String addr = m.get("addr").toString();
        String moduleName = m.get("module_name").toString();
        String name = m.get("name").toString();
        if (moduleName.startsWith("0x")) {
            moduleName = new String(CommonUtils.hexToByteArray(moduleName), StandardCharsets.UTF_8);
        }
        if (name.startsWith("0x")) {
            name = new String(CommonUtils.hexToByteArray(name), StandardCharsets.UTF_8);
        }
        return new StructType(addr, moduleName, name);
    }

    public void handleEvent(Event event, String eventFromAddress) {
        TypeTagStruct eventTypeTagStruct = tryParseTypeTagStruct(event.getTypeTag());
        if (eventTypeTagStruct == null) {
            return;
        }
        // todo 这样过滤事件？
        if ("TokenSwap".equals(eventTypeTagStruct.getModule()) && "AddLiquidityEvent".equals(eventTypeTagStruct.getName())) {
            handleAddLiquidityEvent(event, eventFromAddress, eventTypeTagStruct);
        } else if ("TokenSwapFarm".equals(eventTypeTagStruct.getModule()) && "AddFarmEvent".equals(eventTypeTagStruct.getName())) {
            handleAddFarmEvent(event, eventFromAddress, eventTypeTagStruct);
        } else if ("TokenSwapFarm".equals(eventTypeTagStruct.getModule()) && "StakeEvent".equals(eventTypeTagStruct.getName())) {
            handleStakeEvent(event, eventFromAddress, eventTypeTagStruct);
        } else {
            throw new RuntimeException("Unknown event type: " + event);
        }
    }

    private void handleStakeEvent(Event event, String eventFromAddress, TypeTagStruct eventTypeTagStruct) {
        System.out.println(event);
        //todo
    }

    private void handleAddFarmEvent(Event event, String eventFromAddress, TypeTagStruct eventTypeTagStruct) {
        System.out.println(event);
        //todo
    }

    private void handleAddLiquidityEvent(Event event, String eventFromAddress, TypeTagStruct eventTypeTagStruct) {
        String eventStructAddress = eventTypeTagStruct.getAddress();
        //System.out.println(eventStructAddress);
        String liquidityTokenAddress = eventStructAddress; // todo LiquidityToken 的地址就是事件的结构的地址？
        String liquidityPollAddress = eventFromAddress;// todo 池子的地址就是 event 的来源地址？
        // /////////////////////////////////////
        //String eventData = event.getData();
        //AddLiquidityEvent addLiquidityEvent = null;
//        try {
//            addLiquidityEvent = AddLiquidityEvent.bcsDeserialize(CommonUtils.hexToByteArray(eventData));
//        } catch (DeserializationError deserializationError) {
//            //deserializationError.printStackTrace();
//            LOG.error("AddLiquidityEvent.bcsDeserialize error.", deserializationError);
//            return;
//        }
        //System.out.println(addLiquidityEvent);
//        String xTokenTypeAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.x_token_code.address.value);
//        String xTokenTypeModule = addLiquidityEvent.x_token_code.module;
//        String xTokenTypeName = addLiquidityEvent.x_token_code.name;
//        String yTokenTypeAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.y_token_code.address.value);
//        String yTokenTypeModule = addLiquidityEvent.y_token_code.module;
//        String yTokenTypeName = addLiquidityEvent.y_token_code.name;
//        String accountAddress = CommonUtils.byteListToHexWithPrefix(addLiquidityEvent.signer.value);
//        BigInteger liquidity = addLiquidityEvent.liquidity;
        StructType xTokenType = tokenCodeMapToStructType((Map<String, Object>) event.getDecodeEventData().get("x_token_code"));
        StructType yTokenType = tokenCodeMapToStructType((Map<String, Object>) event.getDecodeEventData().get("y_token_code"));

        String xTokenTypeAddress = xTokenType.getAddress();
        String xTokenTypeModule = xTokenType.getModule();
        String xTokenTypeName = xTokenType.getName();
        String yTokenTypeAddress = yTokenType.getAddress();
        String yTokenTypeModule = yTokenType.getModule();
        String yTokenTypeName = yTokenType.getName();
        String accountAddress = event.getDecodeEventData().get("signer").toString();
        //BigInteger liquidity = new BigInteger(event.getDecodeEventData().get("liquidity").toString());
        Token xToken = tokenService.getTokenByStructType(xTokenTypeAddress, xTokenTypeModule, xTokenTypeName);
        if (xToken == null) {
            LOG.info("Cannot get token by struct type.");
            return;
        }
        Token yToken = tokenService.getTokenByStructType(yTokenTypeAddress, yTokenTypeModule, yTokenTypeName);
        if (yToken == null) {
            LOG.info("Cannot get token by struct type.");
            return;
        }
        LiquidityAccountId liquidityAccountId = new LiquidityAccountId(accountAddress,
                new LiquidityPoolId(
                        new LiquidityTokenId(xToken.getTokenId(), yToken.getTokenId(), liquidityTokenAddress),
                        liquidityPollAddress));
        this.liquidityAccountService.activeLiquidityAccount(liquidityAccountId);
    }

    public static class TypeTagStruct {
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
