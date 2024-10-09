package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.entity.SubscriptionPayment;

@Repository
public interface SubscriptionPaymentRepository extends BaseRepository<SubscriptionPayment, BigDecimal> {
    @Query(value = "SELECT p FROM subscription_payments p WHERE p.paymentStatus = :status " +
            "AND FUNCTION('MONTH', p.createdDate) = FUNCTION('MONTH', :date) " +
            "AND FUNCTION('YEAR', p.createdDate) = FUNCTION('YEAR', :date)")
    Collection<SubscriptionPayment> findByOwnerPaymentIdAndPaymentOwnerType(
            @Param(value = "status") PaymentStatus status,
            @Param(value = "date") LocalDateTime date);
}
