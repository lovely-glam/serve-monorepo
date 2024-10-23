package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingPaymentDetailResponse {
    BigDecimal bookId;
    String nailService;
    String shopName;
    String status;
    List<BookingPaymentResponse> paymentHistory;
}
