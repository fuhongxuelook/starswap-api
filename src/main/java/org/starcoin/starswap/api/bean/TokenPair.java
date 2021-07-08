package org.starcoin.starswap.api.bean;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "token_pair")
@DynamicInsert
public class TokenPair {


    @EmbeddedId
    private TokenPairId tokenPairId;

    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_pair_struct_address"))
    @AttributeOverride(name="module", column=@Column(name="token_pair_struct_module"))
    @AttributeOverride(name="name", column=@Column(name="token_pair_struct_name"))
    private StructTag tokenPairStructTag;


    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_x_struct_address"))
    @AttributeOverride(name="module", column=@Column(name="token_x_struct_module"))
    @AttributeOverride(name="name", column=@Column(name="token_x_struct_name"))
    private StructTag tokenXStructTag;


    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_y_struct_address"))
    @AttributeOverride(name="module", column=@Column(name="token_y_struct_module"))
    @AttributeOverride(name="name", column=@Column(name="token_y_struct_name"))
    private StructTag tokenYStructTag;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String descriptionEn;

    @Column
    private Integer sequenceNumber;

    /**
     * 是否已禁用。
     */
    @Column
    private Boolean deactived;

    @Column(length = 70)
    private String createdBy;

    @Column(length = 70)
    private String updatedBy;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;



}
