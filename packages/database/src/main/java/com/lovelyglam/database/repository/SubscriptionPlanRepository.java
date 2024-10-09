package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.SubscriptionPlan;

@Repository
public interface SubscriptionPlanRepository extends BaseRepository<SubscriptionPlan, BigDecimal> {
    
}
