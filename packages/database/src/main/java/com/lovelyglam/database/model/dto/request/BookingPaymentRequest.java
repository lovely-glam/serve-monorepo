package com.lovelyglam.database.model.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookingPaymentRequest {
    private BigDecimal bookingId;
    private String callbackUrl;
}
