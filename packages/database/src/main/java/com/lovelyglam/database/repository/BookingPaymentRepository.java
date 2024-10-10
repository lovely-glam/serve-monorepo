package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.entity.BookingPayment;

@Repository
public interface BookingPaymentRepository extends BaseRepository<BookingPayment, BigDecimal>  {
    
    @Modifying
    @Query("UPDATE booking_payments b SET b.paymentStatus = :status WHERE b.paymentStatus = :currentStatus AND b.exTime < :now")
    void updatePaymentStatusToTimeout(
        @Param("status") PaymentStatus status,
        @Param("currentStatus") PaymentStatus currentStatus,
        @Param("now") LocalDateTime now
    );
}
