package com.lovelyglam.userserver.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileOutstandingResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;
import com.lovelyglam.database.model.entity.ShopProfile;
import com.lovelyglam.database.model.entity.ShopService;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.NailServiceFeedbackRepository;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.userserver.service.ShopProfileService;
import com.lovelyglam.utils.general.TextUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopProfileServiceImpl implements ShopProfileService {
    private final ShopRepository shopRepository;
    private final NailServiceFeedbackRepository nailServiceFeedbackRepository;

    @Override
    public PaginationResponse<ShopProfileResponse> getShops(SearchRequestParamsDto request) {
        try {
            Page<ShopProfileResponse> orderPage = shopRepository.searchAnyByParameter(request.search(), request.pagination())
                    .map(item -> ShopProfileResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .phone(item.getPhone())
                            .avatarUrl(item.getAvatarUrl())
                            .thumbnails(TextUtils.extractValidUrls(item.getThumbnails()))
                            .address(item.getAddress())
                            .vote(item.getVote())
                            .nailServices(convertShopServiceToNailServiceResponse(item.getShopServices()))
                            .build());
            return convert(orderPage);
        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get ShopProfiles failed with with reason: %s", ex.getMessage()),ex);
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

    @Override
    public ShopProfileResponse getShopDetailById(BigDecimal id) {
        ShopProfile shopProfile = shopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found category with id: %s", id.toString())));
        try {
            var item = shopRepository.save(shopProfile);
            return ShopProfileResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .phone(item.getPhone())
                    .avatarUrl(item.getAvatarUrl())
                    .thumbnails(TextUtils.extractValidUrls(item.getThumbnails()))
                    .address(item.getAddress())
                    .vote(item.getVote())
                    .nailServices(convertShopServiceToNailServiceResponse(item.getShopServices()))
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get ShopProfile failed with with reason: %s", ex.getMessage()),ex);
        }

    }
    @Override
    public List<ShopProfileOutstandingResponse> getShopProfileOutstanding() {
        var queryResult = shopRepository.getTopProfileHaveHighestVote(PageRequest.of(0, 3));
        return queryResult.stream().map(entity -> {
            var feedbackNumber = nailServiceFeedbackRepository.calculateTotalVoteOfShop(entity.getId());
            return ShopProfileOutstandingResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .address(entity.getAddress())
            .feedbackNumber(feedbackNumber)
            .rating(entity.getVote())
            .thumbs(TextUtils.extractValidUrls(entity.getThumbnails()))
            .build();
        }).toList();
    }
    private List<NailServiceResponse> convertShopServiceToNailServiceResponse (List<ShopService> shopServices) {
        if (shopServices == null) return Collections.emptyList();
        return shopServices.stream().map((item) -> {
            return NailServiceResponse.builder()
                    .id(item.getId())
                    .basePrice(item.getBasePrice())
                    .name(item.getName())
                    .description(item.getDescription())
                    .duration(item.getDuration())
                    .build();
        }).toList();
    }
}