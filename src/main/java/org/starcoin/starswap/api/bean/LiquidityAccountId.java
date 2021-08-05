package org.starcoin.starswap.api.bean;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class LiquidityAccountId implements Serializable {
    @Column(length = 50)
    private String accountAddress;

    private TokenPairPoolId tokenPairPoolId = new TokenPairPoolId();

    @Column(length = 50)
    protected String getPoolAddress() {
        return getTokenPairPoolId().getPoolAddress();
    }

    protected void setPoolAddress(String poolAddress) {
        this.getTokenPairPoolId().setPoolAddress(poolAddress);
    }

    @Column(length = 50)
    protected String getTokenXId() {
        return this.getTokenPairPoolId().getTokenPairId().getTokenXId();
    }

    protected void setTokenXId(String tokenXId) {
        this.getTokenPairPoolId().getTokenPairId().setTokenXId(tokenXId);
    }

    @Column(length = 50)
    protected String getTokenYId() {
        return this.getTokenPairPoolId().getTokenPairId().getTokenYId();
    }

    protected void setTokenYId(String tokenYId) {
        this.getTokenPairPoolId().getTokenPairId().setTokenYId(tokenYId);
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public TokenPairPoolId getTokenPairPoolId() {
        return tokenPairPoolId;
    }

    public void setTokenPairPoolId(TokenPairPoolId tokenPairPoolId) {
        this.tokenPairPoolId = tokenPairPoolId;
    }

    public LiquidityAccountId() {
    }

    public LiquidityAccountId(String accountAddress, TokenPairPoolId tokenPairPoolId) {
        this.accountAddress = accountAddress;
        this.tokenPairPoolId = tokenPairPoolId;
    }

    @Override
    public String toString() {
        return "LiquidityAccountId{" +
                "accountAddress='" + accountAddress + '\'' +
                ", tokenPairPoolId=" + tokenPairPoolId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiquidityAccountId that = (LiquidityAccountId) o;
        return Objects.equals(accountAddress, that.accountAddress) && Objects.equals(tokenPairPoolId, that.tokenPairPoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountAddress, tokenPairPoolId);
    }
}
