package com.lovelyglam.utils.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VNPayApiCallback {
    private String orderId;
    private BigDecimal totalPrice;
    private LocalDateTime paymentTime;
    private String transactionId;
    private Boolean paymentStatus;
}
