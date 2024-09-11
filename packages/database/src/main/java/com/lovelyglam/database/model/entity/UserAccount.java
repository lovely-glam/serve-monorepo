package com.lovelyglam.database.model.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_accounts")
public class UserAccount extends BaseEntity{
    @Column(name = "avatar_url", nullable = true, columnDefinition = "VARCHAR(128)")
    private String avatarUrl;
    @Column(name = "fullname", nullable = true, columnDefinition = "VARCHAR(64)")
    private String fullname;
    @Column(name = "email", nullable = true, columnDefinition = "VARCHAR(128)")
    private String email;
    @Column(name = "username", nullable = true, columnDefinition = "VARCHAR(128)")
    private String username;
    @Column(name = "hash_password", nullable = true, columnDefinition = "VARCHAR(128)")
    private String hashPassword;
    @OneToOne(targetEntity = ShopProfile.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "shop_profile_id", nullable = true)
    private ShopProfile shopProfile;
    @OneToMany(targetEntity = LoginMethod.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private List<LoginMethod> loginMethod;
}
