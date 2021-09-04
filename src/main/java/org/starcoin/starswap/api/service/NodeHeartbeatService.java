package org.starcoin.starswap.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.NodeHeartbeat;
import org.starcoin.starswap.api.data.model.Pair;
import org.starcoin.starswap.api.data.repo.NodeHeartbeatRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NodeHeartbeatService {

    private static final Logger LOG = LoggerFactory.getLogger(NodeHeartbeatService.class);

    private final String nodeId = "0x" + UUID.randomUUID().toString().replace("-", "");

    @Autowired
    private NodeHeartbeatRepository nodeHeartbeatRepository;

    public void beat(BigInteger timePoint) {
        NodeHeartbeat nodeHeartbeat = nodeHeartbeatRepository.findById(nodeId).orElse(null);
        if (nodeHeartbeat == null) {
            nodeHeartbeat = new NodeHeartbeat();
            nodeHeartbeat.setNodeId(nodeId);
            nodeHeartbeat.setBeatenAt(timePoint);
        }
        nodeHeartbeat.setBeatenAt(timePoint);
        nodeHeartbeatRepository.save(nodeHeartbeat);
    }

    public List<Pair<BigInteger, BigInteger>> findBreakIntervals() {
        List<Object[]> points = nodeHeartbeatRepository.findBreakpoints();
        List<Pair<BigInteger, BigInteger>> intervals = new ArrayList<>();
        Pair<BigInteger, BigInteger> interval = null;
        for (int i = 0; i < points.size(); i++) {
            Object[] point = points.get(i);
            BigInteger beatenAt = ((BigDecimal)point[0]).toBigInteger();
            int isEndPoint = ((BigInteger)point[1]).intValue();
            if (isEndPoint == 1) {
                interval = new Pair<>();
                interval.setItem1(beatenAt);
            } else if (interval != null && interval.getItem1().compareTo(beatenAt) < 0) {
                interval.setItem2(beatenAt);
                intervals.add(interval);
                interval = null;
            }
        }
        return intervals;
    }

}
