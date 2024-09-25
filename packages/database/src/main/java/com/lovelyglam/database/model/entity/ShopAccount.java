package com.lovelyglam.database.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shop_accounts")
public class ShopAccount extends BaseEntity {
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", columnDefinition = "VARCHAR(128)")
    private String email;
    @Column(name = "hash_password", columnDefinition = "VARCHAR(64)")
    private String hashPassword;
    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive;
    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;
    @OneToOne(targetEntity = ShopProfile.class, fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private ShopProfile shopProfile;
}
