package com.lovelyglam.database.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lovelyglam.database.model.constant.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "subscription_payments")
public class SubscriptionPayment extends BaseEntity {
    @Column(name = "callback_url", nullable = false)
    private String callbackUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @ManyToOne(targetEntity = SubscriptionPlan.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;
// Transaction Information
    @Column(name = "total_payment", nullable = false)
    private BigDecimal totalPayment;
    @Column(name = "ex_time", nullable = false)
    private LocalDateTime exTime;
}
