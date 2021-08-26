package org.starcoin.starswap.api.data.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Token Pair 的 Id。
 */
@Embeddable
public class TokenPairId implements Serializable {

    public TokenPairId() {
    }

    public TokenPairId(String tokenXId, String tokenYId) {
        int i = tokenXId.compareTo(tokenYId);
        if (i < 0) {
            this.tokenXId = tokenXId;
            this.tokenYId = tokenYId;
        } else if (i > 0)  {
            this.tokenYId = tokenXId;
            this.tokenXId = tokenYId;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Column(name = "token_x_id", length = 50, nullable = false)
    private String tokenXId;

    @Column(name = "token_y_id", length = 50, nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenPairId that = (TokenPairId) o;
        return Objects.equals(tokenXId, that.tokenXId) && Objects.equals(tokenYId, that.tokenYId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenXId, tokenYId);
    }
}
