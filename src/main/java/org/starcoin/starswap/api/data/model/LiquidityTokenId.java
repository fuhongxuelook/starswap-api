package org.starcoin.starswap.api.data.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Token Pair 的 Id。
 */
@Embeddable
public class LiquidityTokenId implements Serializable {

    @Column(name = "token_x_id", length = 50, nullable = false)
    private String tokenXId;

    @Column(name = "token_y_id", length = 50, nullable = false)
    private String tokenYId;

    @Column(name = "liquidity_token_address", length = 34, nullable = false)
    private String liquidityTokenAddress;

    public LiquidityTokenId() {
    }

    public LiquidityTokenId(String tokenXId, String tokenYId, String liquidityTokenAddress) {
        this.tokenXId = tokenXId;
        this.tokenYId = tokenYId;
        this.liquidityTokenAddress = liquidityTokenAddress;
    }

    public String getLiquidityTokenAddress() {

        return liquidityTokenAddress;
    }

    public void setLiquidityTokenAddress(String liquidityTokenAddress) {
        this.liquidityTokenAddress = liquidityTokenAddress;
    }

    public String getTokenXId() {
        return tokenXId;
    }

    public void setTokenXId(String tokenXId) {
        this.tokenXId = tokenXId;
    }

    public String getTokenYId() {
        return tokenYId;
    }

    public void setTokenYId(String tokenYId) {
        this.tokenYId = tokenYId;
    }


}
