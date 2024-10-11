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
@Entity(name = "booking_payments")
public class BookingPayment extends BaseEntity {
    @Column(name = "callback_url")
    private String callbackUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @Column(name = "total_payment")
    private BigDecimal totalPayment;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "ex_time")
    private LocalDateTime exTime;
    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;
}
