package com.lovelyglam.database.model.dto.response;

import com.lovelyglam.database.model.constant.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookingPaymentResponse {
    BigDecimal id;
    PaymentStatus paymentStatus;
    BigDecimal totalPayment;
    String shopName;
    String nailService;
    String user;
}
