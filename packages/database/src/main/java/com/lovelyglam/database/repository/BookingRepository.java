package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.Booking;

@Repository
public interface BookingRepository extends BaseRepository<Booking, BigDecimal> {
    
}
