package org.starcoin.starswap.api.taskservice;

import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
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

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.starcoin.starswap.api.data.model.PullingEventTask.PULLING_BLOCK_MAX_COUNT;
import static org.starcoin.starswap.api.utils.JsonRpcUtils.getEvents;

@Service
public class PullingEventTaskTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(PullingEventTaskTaskService.class);
    private final JSONRPC2Session jsonRpcSession;
    private final PullingEventTaskService pullingEventTaskService;
    private final HandleEventService handleEventService;
    private String jsonRpcUrl;

    public PullingEventTaskTaskService(
            @Value("${starcoin.json-rpc-url}") String jsonRpcUrl,
            @Autowired PullingEventTaskService pullingEventTaskService,
            @Autowired HandleEventService handleEventService) throws MalformedURLException {
        this.jsonRpcUrl = jsonRpcUrl;
        this.jsonRpcSession = new JSONRPC2Session(new URL(this.jsonRpcUrl));
        this.pullingEventTaskService = pullingEventTaskService;
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
            Event[] events = getEvents(jsonRpcSession, fromBlockNumber, toBlockNumber);
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

}
