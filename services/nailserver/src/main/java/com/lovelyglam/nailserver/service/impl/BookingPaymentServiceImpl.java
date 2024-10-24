package com.lovelyglam.nailserver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingPaymentResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.BookingPaymentRepository;
import com.lovelyglam.nailserver.service.BookingPaymentService;
import com.lovelyglam.nailserver.utils.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingPaymentServiceImpl implements BookingPaymentService {
    private final BookingPaymentRepository bookingPaymentRepository;
    private final AuthUtils authUtils;
    @Override
    public PaginationResponse<BookingPaymentResponse> getBookingPaymentsByShopId(SearchRequestParamsDto request) {

        var account = authUtils.getUserAccountFromAuthentication();
        try {
            Page<BookingPaymentResponse> orderPage = bookingPaymentRepository
                    .searchByParameter(request.search(), request.pagination(), (param) -> {
                        return (root, query, criteriaBuilder) -> {
                            List<Predicate> predicates = new ArrayList<>();
                            if (param != null && !param.isEmpty()) {
                                for (Map.Entry<String, String> item : param.entrySet()) {
                                    predicates.add(criteriaBuilder.like(
                                            criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                                            "%" + item.getValue().toLowerCase() + "%"));
                                }
                            }
                            predicates.add(criteriaBuilder.equal(root.get("booking").get("shopService").get("shopProfile").get("id"), account.getShopProfile().getId()));

                            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                        };
                    }).map(item -> BookingPaymentResponse.builder()
                            .id(item.getId())
                            .nailService(item.getBooking().getShopService().getName())
                            .paymentStatus(item.getPaymentStatus())
                            .totalPayment(item.getTotalPayment())
                            .user(item.getBooking().getUserAccount().getUsername())
                            .shopName(item.getBooking().getShopService().getName())
                            .build());
            return convert(orderPage);

        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get booking payments failed with reason: %s", ex.getMessage()),ex);
        }
    }

    @Override
    public PaginationResponse<BookingPaymentResponse> getAcceptedBookingPaymentsByShopId(SearchRequestParamsDto request) {
        var account = authUtils.getUserAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("Require system account!!!");
        }
        try {
            Page<BookingPaymentResponse> orderPage = bookingPaymentRepository
                    .searchByParameter(request.search(), request.pagination(), (param) -> {
                        return (root, query, criteriaBuilder) -> {
                            List<Predicate> predicates = new ArrayList<>();
                            if (param != null && !param.isEmpty()) {
                                for (Map.Entry<String, String> item : param.entrySet()) {
                                    predicates.add(criteriaBuilder.like(
                                            criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                                            "%" + item.getValue().toLowerCase() + "%"));
                                }
                            }
                            predicates.add(criteriaBuilder.equal(root.get("booking").get("shopService").get("shopProfile").get("id"), account.getShopProfile().getId()));
                            predicates.add(criteriaBuilder.equal(root.get("booking").get("appointmentStatus"), AppointmentStatus.ACCEPTED));
                            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                        };
                    }).map(item -> BookingPaymentResponse.builder()
                            .id(item.getId())
                            .nailService(item.getBooking().getShopService().getName())
                            .paymentStatus(item.getPaymentStatus())
                            .totalPayment(item.getTotalPayment())
                            .user(item.getBooking().getUserAccount().getUsername())
                            .shopName(item.getBooking().getShopService().getName())
                            .build());
            return convert(orderPage);

        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get booking payments failed with reason: %s", ex.getMessage()),ex);
        }
    }

    public static <T> PaginationResponse<T> convert(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
