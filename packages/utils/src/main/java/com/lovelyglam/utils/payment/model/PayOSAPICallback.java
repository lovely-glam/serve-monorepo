package com.lovelyglam.utils.payment.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayOSAPICallback {
    private String orderId;
    private BigDecimal totalPrice;
    private String orderInfo;
    private Boolean paymentStatus;
    private String transactionId;
}
