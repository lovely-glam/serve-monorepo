package com.lovelyglam.systemserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.dto.response.*;
import com.lovelyglam.database.model.entity.BookingPayment;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.*;
import com.lovelyglam.systemserver.util.AuthUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.systemserver.service.BusinessManagerService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessManagementServiceImpl implements BusinessManagerService {
    private final ShopRepository shopProfileRepository;
    private final BookingRepository bookingRepository;
    private final NailServiceFeedbackRepository nailServiceFeedbackRepository;
    private final BookingPaymentRepository bookingPaymentRepository;
    private final AuthUtils authUtils;

    @Override
    public PaginationResponse<NailProfileManagerResponse> getBusinessProfile(SearchRequestParamsDto request) {
        return shopProfileRepository.searchByParameter(request.search(), request.pagination(), (entity) -> {
            BigDecimal totalProfit = bookingRepository.calculateTotalProfitOfShop(entity.getId());
            Integer totalVote = nailServiceFeedbackRepository.calculateTotalVoteOfShop(entity.getId());
            Double avgVote = nailServiceFeedbackRepository.calculateAverageRatingOfShop(entity.getId());
            return NailProfileManagerResponse.builder()
                    .id(entity.getId())
                    .avatarUrl(entity.getAvatarUrl())
                    .name(entity.getName())
                    .totalVote(totalVote)
                    .averageVote(avgVote)
                    .address(entity.getAddress())
                    .profit(totalProfit)
                    .build();
        }, (param) -> {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                            "%" + item.getValue().toLowerCase() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        });
    }

    @Override
    public PaginationResponse<BookingPaymentResponse> getBookingPayments(SearchRequestParamsDto request) {
        var account = authUtils.getSystemAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("Require system account!!!");
        }
        try {
            Page<BookingPaymentResponse> orderPage = bookingPaymentRepository.searchAnyByParameter(request.search(), request.pagination())
                    .map(item -> BookingPaymentResponse.builder()
                            .id(item.getId())
                            .nailService(item.getBooking().getShopService().getName())
                            .paymentStatus(item.getPaymentStatus())
                            .totalPayment(item.getTotalPayment())
                            .user(item.getBooking().getUserAccount().getUsername())
                            .shopName(item.getBooking().getShopService().getName())
                            .build());
            return convert(orderPage);
        } catch (Exception  ex) {
            throw new ActionFailedException(
                    String.format("Get booking payment failed with with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public PaginationResponse<BookingPaymentResponse> getBookingPaymentsByShopId(SearchRequestParamsDto request, BigDecimal shopId) {

        var account = authUtils.getSystemAccountFromAuthentication();
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
                            predicates.add(criteriaBuilder.equal(root.get("booking").get("shopService").get("shopProfile").get("id"), account.getId()));

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
                    String.format("Get shop services failed with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public Map<String, Object> getCompletedPayment() {
        var account = authUtils.getSystemAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("Require system account!!!");
        }
        List<BookingPayment> allPayments = bookingPaymentRepository.findAll();

        BigDecimal totalSum = BigDecimal.ZERO;
        long count = 0;

        for (BookingPayment payment : allPayments) {
            if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                totalSum = totalSum.add(payment.getTotalPayment());
                count++;
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCount", count);
        summary.put("totalSum", totalSum);

        return summary;
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
