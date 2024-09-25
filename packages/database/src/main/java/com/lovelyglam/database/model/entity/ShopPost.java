package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shop_posts")
public class ShopPost extends BaseEntity {
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "content")
    private String content;
    @Column(name = "images")
    private String images;
    @ManyToOne(targetEntity = ShopProfile.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_profile_id")
    private ShopProfile shopProfile;
}
