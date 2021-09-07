package org.starcoin.starswap.api.taskservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.data.model.PullingEventTask;
import org.starcoin.starswap.api.service.HandleEventService;
import org.starcoin.starswap.api.service.PullingEventTaskService;
import org.starcoin.starswap.api.utils.JsonRpcClient;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.starcoin.starswap.api.data.model.PullingEventTask.PULLING_BLOCK_MAX_COUNT;

@Service
public class PullingEventTaskTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(PullingEventTaskTaskService.class);

    private final PullingEventTaskService pullingEventTaskService;
    private final HandleEventService handleEventService;
    private final JsonRpcClient jsonRpcClient;

    private final String fromAddress;// = "0x598b8cbfd4536ecbe88aa1cfaffa7a62";
    private final String addLiquidityEventTypeTag;// = "0x598b8cbfd4536ecbe88aa1cfaffa7a62::TokenSwap::AddLiquidityEvent";
    private final String addFarmEventTypeTag;// = "0x598b8cbfd4536ecbe88aa1cfaffa7a62::TokenSwapFarm::AddFarmEvent";
    private final String stakeEventTypeTag;// = "0x598b8cbfd4536ecbe88aa1cfaffa7a62::TokenSwapFarm::StakeEvent";


    public PullingEventTaskTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired PullingEventTaskService pullingEventTaskService,
            @Autowired HandleEventService handleEventService,
            @Value("${starcoin.event-filter.from-address}") String fromAddress,
            @Value("${starcoin.event-filter.add-liquidity-event-type-tag}") String addLiquidityEventTypeTag,
            @Value("${starcoin.event-filter.add-farm-event-type-tag}") String addFarmEventTypeTag,
            @Value("${starcoin.event-filter.stake-event-type-tag}") String stakeEventTypeTag) throws MalformedURLException {
        this.jsonRpcClient = new JsonRpcClient(jsonRpcUrl);
        this.pullingEventTaskService = pullingEventTaskService;
        this.handleEventService = handleEventService;
        this.fromAddress = fromAddress;
        this.addLiquidityEventTypeTag = addLiquidityEventTypeTag;
        this.addFarmEventTypeTag = addFarmEventTypeTag;
        this.stakeEventTypeTag = stakeEventTypeTag;
    }

    @Scheduled(fixedDelay = 10000) //todo config
    public void task() {
        List<PullingEventTask> pullingEventTasks = pullingEventTaskService.getPullingEventTaskToProcess();
        if (pullingEventTasks == null || pullingEventTasks.isEmpty()) {
            return;
        }
        for (PullingEventTask t : pullingEventTasks) {
            executeTask(t);
        }
    }

    private void executeTask(PullingEventTask t) {
        BigInteger fromBlockNumber = t.getFromBlockNumber();
        while (fromBlockNumber.compareTo(t.getToBlockNumber()) < 0) {
            BigInteger toBlockNumber = fromBlockNumber.add(BigInteger.valueOf(PULLING_BLOCK_MAX_COUNT));
            if (toBlockNumber.compareTo(t.getToBlockNumber()) > 0) {
                toBlockNumber = t.getToBlockNumber();
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("JSON RPC getting events... from block: " + fromBlockNumber + ", to block: " + toBlockNumber);
            }
            Map<String, Object> eventFilter = createEventFilterMap(fromBlockNumber, toBlockNumber);
            Event[] events = jsonRpcClient.getEvents(eventFilter);
            if (events == null) {
                break;
            }
            for (Event e : events) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Processing a event: " + e);
                }
                handleEventService.handleEvent(e, Event.getFromAddressFromEventKey(e.getEventKey()));//todo get address from eventKey?
            }
            fromBlockNumber = toBlockNumber;
        }
        pullingEventTaskService.updateStatusDone(t);
    }


    private Map<String, Object> createEventFilterMap(BigInteger fromBlockNumber, BigInteger toBlockNumber) {
        Map<String, Object> eventFilter = new HashMap<>();
        eventFilter.put("addr", fromAddress);
        eventFilter.put("type_tags", Arrays.asList(addLiquidityEventTypeTag, addFarmEventTypeTag, stakeEventTypeTag));
        eventFilter.put("decode", true);
        eventFilter.put("from_block", fromBlockNumber);
        eventFilter.put("to_block", toBlockNumber);
        return eventFilter;
    }
}
