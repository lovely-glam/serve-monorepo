package com.lovelyglam.database.repository;

import com.lovelyglam.database.model.entity.ShopService;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository
public interface NailServiceRepository extends BaseRepository<ShopService, BigDecimal> {
}
