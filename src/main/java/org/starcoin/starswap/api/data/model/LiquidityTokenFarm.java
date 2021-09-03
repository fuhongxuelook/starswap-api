package org.starcoin.starswap.api.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigInteger;


@Entity
@Table
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class LiquidityTokenFarm {

    /**
     * Farm Id（领域键）。
     */
    @EmbeddedId
    @AttributeOverride(name = "tokenXId", column = @Column(name = "token_x_id", nullable = false))
    @AttributeOverride(name = "tokenYId", column = @Column(name = "token_y_id", nullable = false))
    @AttributeOverride(name = "liquidityTokenAddress", column = @Column(name = "liquidity_token_address", length = 34, nullable = false))
    @AttributeOverride(name = "farmAddress", column = @Column(name = "farm_address", length = 34, nullable = false))
    private LiquidityTokenFarmId liquidityTokenFarmId;

    @Column(length = 1000, nullable = false)
    private String description;

//    @Column(length = 1000, nullable = false)
//    private String descriptionEn;

    @Column(nullable = false)
    private Integer sequenceNumber;

    @Column(precision = 31, scale = 0)
    private BigInteger totalStakeAmount;

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

    @Version
    private Long version;

    public LiquidityTokenFarmId getLiquidityTokenFarmId() {
        return liquidityTokenFarmId;
    }

    public void setLiquidityTokenFarmId(LiquidityTokenFarmId liquidityTokenFarmId) {
        this.liquidityTokenFarmId = liquidityTokenFarmId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getDescriptionEn() {
//        return descriptionEn;
//    }
//
//    public void setDescriptionEn(String descriptionEn) {
//        this.descriptionEn = descriptionEn;
//    }

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

    public BigInteger getTotalStakeAmount() {
        return totalStakeAmount;
    }

    public void setTotalStakeAmount(BigInteger totalStakeAmount) {
        this.totalStakeAmount = totalStakeAmount;
    }
}