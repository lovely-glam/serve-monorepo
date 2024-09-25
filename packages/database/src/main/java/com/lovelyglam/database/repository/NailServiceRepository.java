package com.lovelyglam.database.repository;

import com.lovelyglam.database.model.entity.ShopService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NailServiceRepository extends BaseRepository<ShopService, BigDecimal> {
}
