package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_boxes")
public class ChatBox extends BaseEntity {
    @ManyToOne(targetEntity = ShopProfile.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopProfile shopProfile;
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;
    @Column(name = "name", nullable = false)
    private String name;
}
