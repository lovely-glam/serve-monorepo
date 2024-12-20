package com.lovelyglam.userserver.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.dto.request.BookingPaymentRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingPaymentDetailResponse;
import com.lovelyglam.database.model.dto.response.BookingPaymentResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.PaymentResponse;
import com.lovelyglam.database.model.entity.BookingPayment;
import com.lovelyglam.database.model.entity.TransactionId;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.BookingPaymentRepository;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.userserver.service.PaymentService;
import com.lovelyglam.userserver.util.AuthUtils;
import com.lovelyglam.utils.payment.model.PayOSAPICallback;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final BookingPaymentRepository bookingPaymentRepository;
    private final BookingRepository bookingRepository;
    private final AuthUtils authUtils;

    public String bookingPaymentTransactionInit(BookingPaymentRequest request,
            Function<BookingPayment, String> cb) {
        var owner = authUtils.getUserAccountFromAuthentication();
        var bookingEntity = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new NotFoundException("Not Found Booking With This ID"));
        if (bookingEntity.getAppointmentStatus() == AppointmentStatus.DONE) {
            throw new ValidationFailedException("This booking is already finished");
        } else if (bookingEntity.getUserAccount().getId() != owner.getId()) {
            throw new ValidationFailedException("This booking is not the right owner");
        }
        var bookingPaymentEntity = BookingPayment.builder()
                .booking(bookingEntity)
                .exTime(LocalDateTime.now().plusMinutes(15))
                .callbackUrl(request.getCallbackUrl())
                .startTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(TransactionId.builder()
                .merchantType(request.getType())
                .build())
                .totalPayment(BigDecimal.valueOf(15000))
                .build();
        try {
            var result = bookingPaymentRepository.save(bookingPaymentEntity);
            return cb.apply(result);
        } catch (Exception ex) {
            throw new ActionFailedException("Failed To Create Payment",ex);
        }
    }

    public PaymentResponse bookingPaymentTransactionCallbackConfirm(VNPayApiCallback callback) {
        BigDecimal transactionId = new BigDecimal(callback.getOrderId());
        PaymentResponse paymentResponse = new PaymentResponse();
        bookingPaymentRepository.findBookingPaymentByTransactionId(transactionId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
                paymentResponse.setStatus(true);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
                paymentResponse.setStatus(false);
            }
            bookingPaymentRepository.save(entity);
            var booking = entity.getBooking();
            if (callback.getPaymentStatus() == true) {
                booking.setAppointmentStatus(AppointmentStatus.BOOKED);
            }else {
                booking.setAppointmentStatus(AppointmentStatus.DENIED);
            }
            bookingRepository.save(booking);
            paymentResponse.setCallbackUrl(entity.getCallbackUrl());
            paymentResponse.setOrderId(callback.getOrderId());
            paymentResponse.setTransactionId(callback.getTransactionId());

        }, () -> {
            throw new ActionFailedException("Not found transaction with this id");
        });
        return paymentResponse;
    }

    public PaymentResponse bookingPaymentTransactionCallbackConfirm(PayOSAPICallback callback) {
        BigDecimal transactionId = new BigDecimal(callback.getOrderId());
        PaymentResponse paymentResponse = new PaymentResponse();
        bookingPaymentRepository.findBookingPaymentByTransactionId(transactionId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
                paymentResponse.setStatus(true);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
                paymentResponse.setStatus(false);
            }
            bookingPaymentRepository.save(entity);
            var booking = entity.getBooking();
            if (callback.getPaymentStatus() == true) {
                booking.setAppointmentStatus(AppointmentStatus.BOOKED);
            }else {
                booking.setAppointmentStatus(AppointmentStatus.DENIED);
            }
            bookingRepository.save(booking);
            paymentResponse.setCallbackUrl(entity.getCallbackUrl());
            paymentResponse.setOrderId(callback.getOrderId());
            paymentResponse.setTransactionId(callback.getTransactionId());

        }, () -> {
            throw new ActionFailedException("Not found transaction with this id");
        });
        return paymentResponse;
    }

    @Override
    @Transactional
    public PaginationResponse<BookingPaymentDetailResponse> getPaymentHistories(SearchRequestParamsDto request) {
        var userProfile = authUtils.getUserAccountFromAuthentication();
        return bookingRepository.searchByParameter(request.search(), request.pagination(), (entity) -> {
            var paymentEntities = entity.getBookingPayments();
            List<BookingPaymentResponse> paymentResponseDto = new ArrayList<>();
            if (paymentEntities != null) {
                paymentResponseDto = paymentEntities.stream().map(item -> {
                    return BookingPaymentResponse.builder()
                    .id(item.getId())
                    .paymentStatus(item.getPaymentStatus())
                    .totalPayment(item.getTotalPayment())
                    .nailService(entity.getShopService().getName())
                    .user(entity.getUserAccount().getFullname())
                    .shopName(entity.getShopService().getShopProfile().getName())
                    .build();
                }).toList();
            }
            String status = entity.getAppointmentStatus() == AppointmentStatus.PENDING? "PENDING" : "PAID";
            return BookingPaymentDetailResponse.builder()
                    .bookId(entity.getId())
                    .nailService(entity.getShopService().getName())
                    .shopName(entity.getShopService().getShopProfile().getName())
                    .status(status)
                    .paymentHistory(paymentResponseDto)
                    .build();
        }, (param) -> {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (param != null && !param.isEmpty()) {
                    for (Map.Entry<String, String> item : param.entrySet()) {
                        predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                                "%" + item.getValue().toLowerCase() + "%"));
                    }
                }
                predicates.add(criteriaBuilder.equal(root.get("userAccount").get("id"), userProfile.getId()));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        });
    }
}
