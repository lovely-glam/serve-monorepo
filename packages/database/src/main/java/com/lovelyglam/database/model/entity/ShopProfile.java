package com.lovelyglam.database.model.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "shop_profiles")
public class ShopProfile extends BaseEntity {
    
}
