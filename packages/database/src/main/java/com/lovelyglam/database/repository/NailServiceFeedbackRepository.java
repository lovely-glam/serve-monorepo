package com.lovelyglam.database.repository;


import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ShopServiceFeedback;
import com.lovelyglam.database.model.entity.ShopServiceFeedbackId;

@Repository
public interface NailServiceFeedbackRepository extends BaseRepository<ShopServiceFeedback, ShopServiceFeedbackId> {
    @Query(value = "SELECT AVG(sf.vote) FROM service_feedbacks sf INNER JOIN sf.id.shopService ss INNER JOIN ss.shopProfile sp WHERE sp.id = :shopId")
    Double calculateAverageRatingOfShop (@Param(value = "shopId") BigDecimal shopId);
    @Query(value = "SELECT SUM(*) FROM service_feedbacks sf INNER JOIN sf.id.shopService ss INNER JOIN ss.shopProfile sp WHERE sp.id = :shopId")
    Integer calculateTotalVoteOfShop (@Param(value = "shopId") BigDecimal shopId);
}
