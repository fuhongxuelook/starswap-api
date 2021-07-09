package org.starcoin.starswap.api.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
public class TokenPairPoolId implements Serializable {
    private TokenPairId tokenPairId = new TokenPairId();

    @Column
    private String poolAddress;

    @Column(length = 200)
    protected String getTokenXId() {
        return this.tokenPairId.getTokenXId();
    }

    protected void setTokenXId(String tokenXId) {
        this.tokenPairId.setTokenXId(tokenXId);
    }

    @Column(length = 200)
    protected String getTokenYId() {
        return this.tokenPairId.getTokenYId();
    }

    protected void setTokenYId(String tokenYId) {
        this.tokenPairId.setTokenYId(tokenYId);
    }




    public TokenPairId getTokenPairId() {
        return tokenPairId;
    }

    public void setTokenPairId(TokenPairId tokenPairId) {
        this.tokenPairId = tokenPairId;
    }

    public String getPoolAddress() {
        return poolAddress;
    }

    public void setPoolAddress(String poolAddress) {
        this.poolAddress = poolAddress;
    }

    public TokenPairPoolId() {
    }

    public TokenPairPoolId(TokenPairId tokenPairId, String poolAddress) {
        this.tokenPairId = tokenPairId;
        this.poolAddress = poolAddress;
    }

    public TokenPairPoolId(String tokenXId, String tokenYId, String poolAddress) {
        this.tokenPairId = new TokenPairId(tokenXId, tokenYId);
        this.poolAddress = poolAddress;
    }

    @Override
    public String toString() {
        return "TokenPairPoolId{" +
                "tokenPairId=" + tokenPairId +
                ", poolAddress='" + poolAddress + '\'' +
                '}';
    }
}
