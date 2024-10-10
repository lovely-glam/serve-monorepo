package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.BookingPayment;

@Repository
public interface BookingPaymentRepository extends BaseRepository<BookingPayment, BigDecimal>  {
    
}
