package org.starcoin.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
    @JsonProperty("block_hash")
    @JSONField(name = "block_hash")
    String blockHash;

    @JSONField(name = "block_number")
    @JsonProperty("block_number")
    String blockNumber;

    @JSONField(name = "transaction_hash")
    @JsonProperty("transaction_hash")
    String transactionHash;

    @JSONField(name = "transaction_index")
    @JsonProperty("transaction_index")
    int transactionIndex;

    String data;

    @JSONField(name = "type_tag")
    @JsonProperty("type_tag")
    TypeTag typeTag;

    @JSONField(name = "event_key")
    @JsonProperty("event_key")
    String eventKey;

    @JSONField(name = "event_seq_number")
    @JsonProperty("event_seq_number")
    String eventSeqNumber;

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public int getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(int transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public TypeTag getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(TypeTag typeTag) {
        this.typeTag = typeTag;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventSeqNumber() {
        return eventSeqNumber;
    }

    public void setEventSeqNumber(String eventSeqNumber) {
        this.eventSeqNumber = eventSeqNumber;
    }

    @Override
    public String toString() {
        return "Event{" +
                "blockHash='" + blockHash + '\'' +
                ", blockNumber='" + blockNumber + '\'' +
                ", transactionHash='" + transactionHash + '\'' +
                ", transactionIndex=" + transactionIndex +
                ", data='" + data + '\'' +
                ", typeTag='" + typeTag + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", eventSeqNumber='" + eventSeqNumber + '\'' +
                '}';
    }
}
