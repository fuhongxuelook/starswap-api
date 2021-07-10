package org.starcoin.starswap.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "token_pair")
@DynamicInsert
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class TokenPair {


    @EmbeddedId
    private TokenPairId tokenPairId;

//    @Embedded
//    @AttributeOverride(name="address", column=@Column(name="token_pair_struct_address", nullable = false))
//    @AttributeOverride(name="module", column=@Column(name="token_pair_struct_module", nullable = false))
//    @AttributeOverride(name="name", column=@Column(name="token_pair_struct_name", nullable = false))
//    private StructType tokenPairStructType;

    @Column
    private String defaultPoolAddress;

    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_x_struct_address", nullable = false))
    @AttributeOverride(name="module", column=@Column(name="token_x_struct_module", nullable = false))
    @AttributeOverride(name="name", column=@Column(name="token_x_struct_name", nullable = false))
    private StructType tokenXStructType;


    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_y_struct_address", nullable = false))
    @AttributeOverride(name="module", column=@Column(name="token_y_struct_module", nullable = false))
    @AttributeOverride(name="name", column=@Column(name="token_y_struct_name", nullable = false))
    private StructType tokenYStructType;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(length = 1000, nullable = false)
    private String descriptionEn;

    @Column(nullable = false)
    private Integer sequenceNumber;

    /**
     * 是否已禁用。
     */
    @Column(nullable = false)
    private Boolean deactived;

    @Column(length = 70, nullable = false)
    private String createdBy;

    @Column(length = 70, nullable = false)
    private String updatedBy;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    public TokenPairId getTokenPairId() {
        return tokenPairId;
    }

    public void setTokenPairId(TokenPairId tokenPairId) {
        this.tokenPairId = tokenPairId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getDeactived() {
        return deactived;
    }

    public void setDeactived(Boolean deactived) {
        this.deactived = deactived;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getDefaultPoolAddress() {
        return defaultPoolAddress;
    }

    public void setDefaultPoolAddress(String defaultPoolAddress) {
        this.defaultPoolAddress = defaultPoolAddress;
    }

    public StructType getTokenXStructType() {
        return tokenXStructType;
    }

    public void setTokenXStructType(StructType tokenXStructType) {
        this.tokenXStructType = tokenXStructType;
    }

    public StructType getTokenYStructType() {
        return tokenYStructType;
    }

    public void setTokenYStructType(StructType tokenYStructType) {
        this.tokenYStructType = tokenYStructType;
    }

    @Override
    public String toString() {
        return "TokenPair{" +
                "tokenPairId=" + tokenPairId +
                ", defaultPoolAddress=" + defaultPoolAddress +
                ", tokenXStructType=" + tokenXStructType +
                ", tokenYStructType=" + tokenYStructType +
                ", description='" + description + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", deactived=" + deactived +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
