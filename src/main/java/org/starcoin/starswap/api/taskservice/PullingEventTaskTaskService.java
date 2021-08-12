package org.starcoin.starswap.api.taskservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import org.springframework.stereotype.Service;
import org.starcoin.bean.Event;
import org.starcoin.starswap.api.bean.PullingEventTask;
import org.starcoin.starswap.api.dao.PullingEventTaskRepository;
import org.starcoin.starswap.api.service.HandleEventService;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PullingEventTaskTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(PullingEventTaskTaskService.class);

    private static final int PULLING_BLOCK_MAX_COUNT = 32;

    private String jsonRpcUrl;

    private JSONRPC2Session jsonRpcSession;

    private PullingEventTaskRepository pullingEventTaskRepository;

    private HandleEventService handleEventService;

    public String getJsonRpcUrl() {
        return jsonRpcUrl;
    }

    public void setJsonRpcUrl(String jsonRpcUrl) {
        this.jsonRpcUrl = jsonRpcUrl;
    }

    public PullingEventTaskTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired PullingEventTaskRepository pullingEventTaskRepository,
            @Autowired HandleEventService handleEventService) throws MalformedURLException {
        this.jsonRpcUrl = jsonRpcUrl;
        this.jsonRpcSession = new JSONRPC2Session(new URL(this.jsonRpcUrl));
        this.pullingEventTaskRepository = pullingEventTaskRepository;
        this.handleEventService = handleEventService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void task() {
        List<PullingEventTask> pullingEventTasks = pullingEventTaskRepository.findByStatusEquals(PullingEventTask.STATUS_CREATED);
        if (pullingEventTasks == null || pullingEventTasks.isEmpty()) {
            return;
        }
        for (PullingEventTask t : pullingEventTasks) {
            BigInteger fromBlockNumber = t.getFromBlockNumber();
            while (fromBlockNumber.compareTo(t.getToBlockNumber()) < 0) {
                BigInteger toBlockNumber = fromBlockNumber.add(BigInteger.valueOf(PULLING_BLOCK_MAX_COUNT));
                if (toBlockNumber.compareTo(t.getToBlockNumber()) > 0) {
                    toBlockNumber = t.getToBlockNumber();
                }
                LOG.debug("From block: " + fromBlockNumber + ", to block: " + toBlockNumber);
                Event[] events = rpcGetEvents(fromBlockNumber, toBlockNumber);
                if (events == null) {
                    return;
                }
                for (Event e : events) {
                    LOG.debug("JSON rpc get events: " + e);
                    handleEventService.handleEvent(e);
                }
                fromBlockNumber = toBlockNumber;
            }
            t.done();
            t.setUpdatedBy("ADMIN");
            t.setUpdatedAt(System.currentTimeMillis());
            pullingEventTaskRepository.save(t);
        }
    }

    private Event[] rpcGetEvents(BigInteger fromBlockNumber, BigInteger toBlockNumber) {
        String method = "chain.get_events";
        Map<String, Object> eventFilter = new HashMap<>();
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
                    LOG.error("JSON rpc result parse error.", e);
                    return null;
                }
            }
        }
        return events;
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
