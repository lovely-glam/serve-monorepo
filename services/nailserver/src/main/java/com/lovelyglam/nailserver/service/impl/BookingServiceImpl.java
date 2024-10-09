package com.lovelyglam.nailserver.service.impl;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.database.repository.NailServiceRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.nailserver.service.BookingService;
import com.lovelyglam.nailserver.utils.AuthUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final NailServiceRepository nailServiceRepository;
    private final UserAccountRepository userAccountRepository;
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
                            predicates.add(criteriaBuilder.equal(root.get("shopService").get("shopProfile").get("id"), account.getId()));

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
    public PaginationResponse<BookingResponse> getBookingsByTime(SearchRequestParamsDto request) {
        return null;
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        return null;
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