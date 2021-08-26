package org.starcoin.starswap.api.data.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TokenPairPoolId implements Serializable {

    @Column(length = 50)
    private String poolAddress;

    private TokenPairId tokenPairId = new TokenPairId();

    @Column(length = 50)
    protected String getTokenXId() {
        return this.getTokenPairId().getTokenXId();
    }

    protected void setTokenXId(String tokenXId) {
        this.getTokenPairId().setTokenXId(tokenXId);
    }

    @Column(length = 50)
    protected String getTokenYId() {
        return this.getTokenPairId().getTokenYId();
    }

    protected void setTokenYId(String tokenYId) {
        this.getTokenPairId().setTokenYId(tokenYId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenPairPoolId that = (TokenPairPoolId) o;
        return Objects.equals(poolAddress, that.poolAddress) && Objects.equals(tokenPairId, that.tokenPairId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(poolAddress, tokenPairId);
    }
}
