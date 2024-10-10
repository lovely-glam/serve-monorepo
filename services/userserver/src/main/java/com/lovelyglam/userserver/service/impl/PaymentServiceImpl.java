package com.lovelyglam.userserver.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.dto.response.PaymentResponse;
import com.lovelyglam.database.model.entity.BookingPayment;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.BookingPaymentRepository;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.userserver.service.PaymentService;
import com.lovelyglam.userserver.util.AuthUtils;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final BookingPaymentRepository bookingPaymentRepository;
    private final BookingRepository bookingRepository;
    private final AuthUtils authUtils;

    public String bookingPaymentTransactionInit (BigDecimal bookingId, String callbackUrl, Function<BookingPayment, String> cb) {
        var owner = authUtils.getUserAccountFromAuthentication();
        var bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Not Found Booking With This ID"));
        if (bookingEntity.getAppointmentStatus() == AppointmentStatus.DONE) {
            throw new ValidationFailedException("This booking is already finished");
        }else if (bookingEntity.getUserAccount().getId() != owner.getId()) {
            throw new ValidationFailedException("This booking is not the right owner");
        }
        var bookingPaymentEntity = BookingPayment.builder()
            .booking(bookingEntity)
            .exTime(LocalDateTime.now().plusMinutes(15))
            .callbackUrl(callbackUrl)
            .startTime(LocalDateTime.now())
            .paymentStatus(PaymentStatus.PENDING)
            .totalPayment(BigDecimal.valueOf(10000))
        .build();
        try {
            var result = bookingPaymentRepository.save(bookingPaymentEntity);
            return cb.apply(result);
        }catch (Exception ex) {
            throw new ActionFailedException("Failed To Create Payment");
        }
    }

    
    public PaymentResponse bookingPaymentTransactionCallbackConfirm (VNPayApiCallback callback) {
        BigDecimal transactionId = new BigDecimal(callback.getOrderId());
        PaymentResponse paymentResponse = new PaymentResponse();
        bookingPaymentRepository.findById(transactionId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
            }
            bookingPaymentRepository.save(entity);
            var booking = entity.getBooking();
            booking.setAppointmentStatus(AppointmentStatus.BOOKED);
            bookingRepository.save(booking);
            paymentResponse.setCallbackUrl(entity.getCallbackUrl());
            paymentResponse.setStatus(true);
            paymentResponse.setOrderId(callback.getOrderId());
            paymentResponse.setTransactionId(callback.getTransactionId());

        }, () -> {
            paymentResponse.setStatus(false);
        });
        return paymentResponse;
    }
}