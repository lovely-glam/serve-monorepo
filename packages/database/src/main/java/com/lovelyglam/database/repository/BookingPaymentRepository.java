package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import com.lovelyglam.database.model.entity.SubscriptionPayment;
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

    @Query("SELECT b FROM booking_payments b WHERE b.paymentStatus = :status")
    Collection<BookingPayment> findAllByPaymentStatus(@Param("status") PaymentStatus status);

    @Query("SELECT COUNT(b) FROM booking_payments b WHERE b.paymentStatus = :status")
    long countByPaymentStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(b.totalPayment) FROM booking_payments b WHERE b.paymentStatus = :status")
    BigDecimal sumTotalPaymentByPaymentStatus(@Param("status") PaymentStatus status);



}
