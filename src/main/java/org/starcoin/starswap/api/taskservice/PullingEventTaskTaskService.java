package org.starcoin.starswap.api.taskservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.data.model.PullingEventTask;
import org.starcoin.starswap.api.data.repo.PullingEventTaskRepository;
import org.starcoin.starswap.api.service.HandleEventService;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.starcoin.starswap.api.data.model.PullingEventTask.PULLING_BLOCK_MAX_COUNT;
import static org.starcoin.starswap.subscribe.StarcoinEventSubscriber.createEventFilterMap;

@Service
public class PullingEventTaskTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(PullingEventTaskTaskService.class);
    private final JSONRPC2Session jsonRpcSession;
    private final PullingEventTaskRepository pullingEventTaskRepository;
    private final HandleEventService handleEventService;
    private String jsonRpcUrl;

    public PullingEventTaskTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired PullingEventTaskRepository pullingEventTaskRepository,
            @Autowired HandleEventService handleEventService) throws MalformedURLException {
        this.jsonRpcUrl = jsonRpcUrl;
        this.jsonRpcSession = new JSONRPC2Session(new URL(this.jsonRpcUrl));
        this.pullingEventTaskRepository = pullingEventTaskRepository;
        this.handleEventService = handleEventService;
    }

    public String getJsonRpcUrl() {
        return jsonRpcUrl;
    }

    public void setJsonRpcUrl(String jsonRpcUrl) {
        this.jsonRpcUrl = jsonRpcUrl;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void task() {
        List<PullingEventTask> pullingEventTasks = pullingEventTaskRepository.findByStatusEquals(PullingEventTask.STATUS_CREATED);
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
            Event[] events = rpcGetEvents(fromBlockNumber, toBlockNumber);
            if (events == null) {
                break;
            }
            for (Event e : events) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Processing a event: " + e);
                }
                handleEventService.handleEvent(e);
            }
            fromBlockNumber = toBlockNumber;
        }
        t.done();
        t.setUpdatedBy("ADMIN");
        t.setUpdatedAt(System.currentTimeMillis());
        pullingEventTaskRepository.save(t);
    }

    private Event[] rpcGetEvents(BigInteger fromBlockNumber, BigInteger toBlockNumber) {
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

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


}
