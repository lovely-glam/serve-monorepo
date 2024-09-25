package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shop_profiles")
public class ShopProfile extends BaseEntity {
    @Column(name = "name", columnDefinition = "VARCHAR(128)")
    private String name;
    @Column(name = "avatar", columnDefinition = "VARCHAR(256)")
    private String avatarUrl;
    @Column(name = "thumbnails", columnDefinition = "VARCHAR(1028)")
    private String thumbnails;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "address", columnDefinition = "VARCHAR(256)")
    private String address;
    @Column(name = "phone", columnDefinition = "VARCHAR(12)")
    private String phone;
    @OneToOne(targetEntity = ShopAccount.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_account_id")
    private ShopAccount account;
}
