package com.lovelyglam.database.repository;


import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ShopServiceFeedback;
import com.lovelyglam.database.model.entity.ShopServiceFeedbackId;

@Repository
public interface NailServiceFeedbackRepository extends BaseRepository<ShopServiceFeedback, ShopServiceFeedbackId> {
    
}
