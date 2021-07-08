package org.starcoin.starswap.api.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TokenPairId implements Serializable {

    @Column(name = "token_x_id", length = 200, nullable = false)
    private String tokenXId;

    @Column(name = "token_y_id", length = 200, nullable = false)
    private String tokenYId;

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

    @Override
    public String toString() {
        return "TokenPairId{" +
                "tokenXId='" + tokenXId + '\'' +
                ", tokenYId='" + tokenYId + '\'' +
                '}';
    }
}
