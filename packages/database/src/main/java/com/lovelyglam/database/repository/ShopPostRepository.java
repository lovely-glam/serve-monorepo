package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ShopPost;

@Repository
public interface ShopPostRepository extends BaseRepository<ShopPost, BigDecimal> {
    
}
