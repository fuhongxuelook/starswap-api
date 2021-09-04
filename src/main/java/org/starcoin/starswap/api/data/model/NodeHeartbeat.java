package org.starcoin.starswap.api.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Entity
public class NodeHeartbeat {

    @Id
    @Column(length = 34)
    private String nodeId;

    @Column(precision = 21, scale = 0)
    private BigInteger startedAt;

    @Column(precision = 21, scale = 0)
    private BigInteger beatenAt;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public BigInteger getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(BigInteger startedAt) {
        this.startedAt = startedAt;
    }

    public BigInteger getBeatenAt() {
        return beatenAt;
    }

    public void setBeatenAt(BigInteger beatenAt) {
        this.beatenAt = beatenAt;
    }
}
