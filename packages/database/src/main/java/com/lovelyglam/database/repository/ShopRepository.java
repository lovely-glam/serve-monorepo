package com.lovelyglam.database.repository;

import com.lovelyglam.database.model.entity.ShopProfile;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends BaseRepository<ShopProfile, BigDecimal> {
    @Modifying
    @Query("UPDATE shop_profiles sp SET sp.vote = " +
            "(SELECT COALESCE(AVG(ssf.vote), 0.0) FROM service_feedbacks ssf " +
            "JOIN ssf.id.shopService s ON s.shopProfile.id = sp.id) " +
            "WHERE sp.id = sp.id")
    void updateAllAverageVotes();
}
