package com.lovelyglam.userserver.service;

import java.math.BigDecimal;
import java.util.function.Function;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingPaymentDetailResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.PaymentResponse;
import com.lovelyglam.database.model.entity.BookingPayment;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

public interface PaymentService {
    String bookingPaymentTransactionInit (BigDecimal bookingId, String callbackUrl, Function<BookingPayment, String> cb);
    PaymentResponse bookingPaymentTransactionCallbackConfirm (VNPayApiCallback callback);
    PaginationResponse<BookingPaymentDetailResponse> getPaymentHistories(SearchRequestParamsDto request);
}
