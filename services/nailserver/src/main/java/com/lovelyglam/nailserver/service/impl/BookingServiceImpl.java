package com.lovelyglam.nailserver.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.entity.Booking;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.nailserver.service.BookingService;
import com.lovelyglam.nailserver.utils.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AuthUtils authUtils;

    @Override
    public PaginationResponse<BookingResponse> getBookingsByShopId(SearchRequestParamsDto request) {
        var account = authUtils.getUserAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("No Account Login");
        }

        try {
            Page<BookingResponse> orderPage = bookingRepository
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
                            predicates.add(criteriaBuilder.equal(root.get("shopService").get("shopProfile").get("id"), account.getShopProfile().getId()));

                            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                        };
                    }).map(item -> BookingResponse.builder()
                            .id(item.getId())
                            .userAccountName(item.getUserAccount().getFullname())
                            .shopServiceName(item.getShopService().getName())
                            .startTime(item.getStartTime())
                            .makingDay(item.getMakingDay())
                            .status(item.getAppointmentStatus())
                            .build());
            return convert(orderPage);

        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get bookings failed with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public Collection<BookingResponse> getBookingsByDayAndShopId(Date makingDate) {
        var account = authUtils.getUserAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("No Account Login");
        }
        try {
            Collection<Booking> bookings = bookingRepository.getBookingsByPaymentStatusAndMakingDay(account.getShopProfile().getId(), AppointmentStatus.ACCEPTED, makingDate);

            return convertToBookingResponse(bookings);

        } catch (Exception e) {
            throw new ActionFailedException(
                    String.format("Get bookings failed with reason: %s", e.getMessage()));
        }
    }

    @Override
    public Collection<BookingResponse> getBookingsByStartTimeAndShopId(Timestamp startTime) {
        var account = authUtils.getUserAccountFromAuthentication();
        if (account == null) {
            throw new AuthFailedException("No Account Login");
        }

        try {
            Collection<Booking> bookings = bookingRepository.getBookingsByPaymentStatusAndMakingTime(account.getShopProfile().getId(), AppointmentStatus.ACCEPTED, startTime);

            return convertToBookingResponse(bookings);

        } catch (Exception e) {
            throw new ActionFailedException(
                    String.format("Get bookings failed with reason: %s", e.getMessage()));
        }
    }

    public Collection<BookingResponse> convertToBookingResponse(Collection<Booking> bookings) {
        return bookings.stream()
                .map(booking -> BookingResponse.builder()
                        .id(booking.getId()) // Assuming id is BigDecimal, or cast appropriately
                        .shopServiceName(booking.getShopService().getName()) // Assuming ShopService has a getName() method
                        .userAccountName(booking.getUserAccount().getFullname()) // Assuming UserAccount has a getUsername() method
                        .makingDay(booking.getMakingDay())
                        .startTime(booking.getStartTime())
                        .status(booking.getAppointmentStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(BigDecimal id) {
        var response = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found booking with this id"));
        return BookingResponse.builder()
            .id(response.getId())
            .shopServiceName(response.getShopService().getName())
            .userAccountName(response.getUserAccount().getFullname())
            .makingDay(response.getMakingDay())
            .startTime(response.getStartTime())
            .status(response.getAppointmentStatus())
        .build();
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