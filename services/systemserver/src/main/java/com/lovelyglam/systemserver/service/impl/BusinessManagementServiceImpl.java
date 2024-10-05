package com.lovelyglam.systemserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailProfileManagerResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.database.repository.NailServiceFeedbackRepository;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.systemserver.service.BusinessManagerService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessManagementServiceImpl implements BusinessManagerService {
    private final ShopRepository shopProfileRepository;
    private final BookingRepository bookingRepository;
    private final NailServiceFeedbackRepository nailServiceFeedbackRepository;

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

}
