package com.lovelyglam.database.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
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
@Entity(name = "shop_services")
public class ShopService extends BaseEntity{
    @ManyToOne(targetEntity = ShopProfile.class, fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "shop_profile_id", nullable = false)
    private ShopProfile shopProfile;
    @Column(name = "name")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "base_price")    
    private BigDecimal basePrice;
    @Column(name = "duration")
    private Long duration;
}
