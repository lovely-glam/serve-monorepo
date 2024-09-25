package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ServiceBooking;

@Repository
public interface ServiceBookingRepository extends BaseRepository<ServiceBooking, BigDecimal> {
    
}
