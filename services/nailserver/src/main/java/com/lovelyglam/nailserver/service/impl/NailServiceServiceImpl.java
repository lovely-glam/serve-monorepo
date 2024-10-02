package com.lovelyglam.nailserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.NailServiceRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.NailServiceDetailResponse;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.entity.ShopService;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.database.repository.NailServiceRepository;
import com.lovelyglam.nailserver.service.NailServiceService;
import com.lovelyglam.nailserver.utils.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailServiceServiceImpl implements NailServiceService {
    private final NailServiceRepository nailServiceRepository;
    private final BookingRepository bookingRepository;
    private final AuthUtils authUtils;

    @Override
    public NailServiceDetailResponse getServiceDetailById(BigDecimal id) {
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        var currentShopService = nailServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found nail service with this id"));
        if (shopProfile.equals(currentShopService.getShopProfile())) {
            var bookingNotFinish = bookingRepository.findBookingByStatusAndServiceId(id,
                    List.of(AppointmentStatus.BOOKED, AppointmentStatus.ACCEPTED)).stream().map((item) -> {
                        return BookingResponse.builder()
                                .id(item.getId())
                                .shopServiceName(currentShopService.getName())
                                .userAccountName(item.getUserAccount().getFullname())
                                .makingDay(item.getMakingDay())
                                .startTime(item.getStartTime())
                                .build();
                    }).toList();
            return NailServiceDetailResponse.builder()
                    .basePrice(currentShopService.getBasePrice())
                    .name(currentShopService.getName())
                    .duration(currentShopService.getDuration())
                    .description(currentShopService.getDescription())
                    .id(currentShopService.getId())
                    .bookingList(bookingNotFinish)
                    .build();
        }
        throw new ValidationFailedException("This service is not your own");
    }

    @Override
    public NailServiceResponse createNailService(NailServiceRequest request) {
        if (request == null)
            throw new ValidationFailedException("Request is not validate");
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        var shopServiceImport = ShopService.builder()
                .basePrice(request.getBasePrice())
                .shopProfile(shopProfile)
                .name(request.getName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .build();
        try {
            var result = nailServiceRepository.save(shopServiceImport);
            return NailServiceResponse.builder()
                    .basePrice(result.getBasePrice())
                    .name(result.getName())
                    .duration(result.getDuration())
                    .description(result.getDescription())
                    .id(result.getId())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Can't create nail service");
        }
    }

    @Override
    public NailServiceResponse updateNailService(NailServiceRequest request, BigDecimal id) {
        if (request == null)
            throw new ValidationFailedException("Request is not validate");
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        var currentShopService = nailServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found nail service with this id"));
        if (!shopProfile.equals(currentShopService.getShopProfile()))
            throw new ValidationFailedException("This service is not your own");
        currentShopService.setBasePrice(request.getBasePrice());
        currentShopService.setName(request.getName());
        currentShopService.setDescription(request.getDescription());
        currentShopService.setDuration(request.getDuration());
        try {
            var result = nailServiceRepository.save(currentShopService);
            return NailServiceResponse.builder()
                    .basePrice(result.getBasePrice())
                    .name(result.getName())
                    .duration(result.getDuration())
                    .description(result.getDescription())
                    .id(result.getId())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Can't update nail service");
        }
    }

    @Override
    public PaginationResponse<NailServiceResponse> getNailService(SearchRequestParamsDto request) {
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        return nailServiceRepository.searchByParameter(request.search(), request.pagination(), (result) -> {
            return NailServiceResponse.builder()
            .basePrice(result.getBasePrice())
            .name(result.getName())
            .duration(result.getDuration())
            .description(result.getDescription())
            .id(result.getId()).build();
        }, (param) -> {            
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("shopProfile"), shopProfile));
    
                if (param != null && !param.isEmpty()) {
                    for (Map.Entry<String, String> item : param.entrySet()) {
                        predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                                "%" + item.getValue().toLowerCase() + "%"
                        ));
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        });
    }
}
