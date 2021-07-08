package org.starcoin.starswap.api.bean;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "token")
@DynamicInsert
public class Token {

    /**
     * Token 的 Id。一般应该是缩写，同样的缩写只应该允许注册一次，防止混淆。
     */
    @Id
    @Column(length = 200, nullable = false, unique = true)
    private String tokenId;

    @Embedded
    @AttributeOverride(name="address", column=@Column(name="token_struct_address"))
    @AttributeOverride(name="module", column=@Column(name="token_struct_module"))
    @AttributeOverride(name="name", column=@Column(name="token_struct_name"))
    private StructTag tokenStructTag;

    @Column(length = 1000)
    private String iconUrl;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String descriptionEn;

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
