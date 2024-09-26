package com.lovelyglam.database.repository;

import com.lovelyglam.database.model.entity.ShopProfile;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends BaseRepository<ShopProfile, BigDecimal>{
}
