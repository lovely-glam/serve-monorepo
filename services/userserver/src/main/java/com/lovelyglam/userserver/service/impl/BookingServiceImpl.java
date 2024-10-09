package com.lovelyglam.userserver.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.BookingRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.entity.Booking;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.database.repository.NailServiceRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.userserver.service.BookingService;
import com.lovelyglam.userserver.util.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final NailServiceRepository nailServiceRepository;
    private final UserAccountRepository userAccountRepository;
    private final AuthUtils authUtils;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        var nailService = nailServiceRepository.findById(bookingRequest.getNailServiceId())
                .orElseThrow(() -> new NotFoundException("Not found nail service"));
        Booking booking = Booking.builder()
                .shopService(nailService)
                .startTime(bookingRequest.getStartTime())
                .appointmentStatus(bookingRequest.getStatus())
                .userAccount(authUtils.getUserAccountFromAuthentication())
                .startTime(bookingRequest.getStartTime())
                .makingDay(bookingRequest.getMakingDay())
                .appointmentStatus(AppointmentStatus.BOOKED)
                .build();
        try {
            var item = bookingRepository.save(booking);
            return BookingResponse.builder()
                    .id(item.getId())
                    .userAccountName(item.getUserAccount().getFullname())
                    .shopServiceName(item.getShopService().getName())
                    .startTime(item.getStartTime())
                    .makingDay(item.getMakingDay())
                    .status(item.getAppointmentStatus())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Create booking failed with with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public PaginationResponse<BookingResponse> getBookings(SearchRequestParamsDto request) {
        try {
            Page<BookingResponse> orderPage = bookingRepository
                    .searchAnyByParameter(request.search(), request.pagination())
                    .map(item -> BookingResponse.builder()
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
                    String.format("Get NailServices failed with with reason: %s", ex.getMessage()));
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
                page.isLast());
    }

    @Override
    public BookingResponse getBooking(BigDecimal bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Not found booking with id: %s", bookingId.toString())));
        try {
            var item = bookingRepository.save(booking);
            return BookingResponse.builder()
                    .id(item.getId())
                    .userAccountName(item.getUserAccount().getFullname())
                    .shopServiceName(item.getShopService().getName())
                    .startTime(item.getStartTime())
                    .makingDay(item.getMakingDay())
                    .status(item.getAppointmentStatus())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get Booking failed with with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public BookingResponse updateBooking(BigDecimal bookingId, BookingRequest bookingRequest) {
        Booking bookingDb = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Not Found Booking"));
        bookingDb.setStartTime(bookingRequest.getStartTime());
        bookingDb.setUpdatedDate(LocalDateTime.now());
        bookingDb.setAppointmentStatus(bookingRequest.getStatus());
        try {
            var item = bookingRepository.save(bookingDb);
            return BookingResponse.builder()
                    .id(item.getId())
                    .userAccountName(item.getUserAccount().getFullname())
                    .shopServiceName(item.getShopService().getName())
                    .startTime(item.getStartTime())
                    .makingDay(item.getMakingDay())
                    .status(item.getAppointmentStatus())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed update booking with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public BookingResponse disableBooking(BigDecimal bookingId) {
        Booking bookingDb = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Not Found Booking"));
        bookingDb.setAppointmentStatus(AppointmentStatus.DENIED);
        try {
            var item = bookingRepository.save(bookingDb);
            return BookingResponse.builder()
                    .id(item.getId())
                    .userAccountName(item.getUserAccount().getFullname())
                    .shopServiceName(item.getShopService().getName())
                    .startTime(item.getStartTime())
                    .makingDay(item.getMakingDay())
                    .status(item.getAppointmentStatus())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed disable booking with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public PaginationResponse<BookingResponse> getBookingsByUserId(SearchRequestParamsDto request) {
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
                            predicates.add(criteriaBuilder.equal(root.get("userAccount").get("id"), account.getId()));

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
                    String.format("Get shop services failed with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public BookingResponse acceptBooking(BigDecimal bookingId) {
        Booking bookingDb = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Not Found Booking"));
        bookingDb.setAppointmentStatus(AppointmentStatus.ACCEPTED);
        try {
            var item = bookingRepository.save(bookingDb);
            return BookingResponse.builder()
                    .id(item.getId())
                    .userAccountName(item.getUserAccount().getFullname())
                    .shopServiceName(item.getShopService().getName())
                    .startTime(item.getStartTime())
                    .makingDay(item.getMakingDay())
                    .status(item.getAppointmentStatus())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed accept booking with reason: %s", ex.getMessage()));
        }
    }
}
