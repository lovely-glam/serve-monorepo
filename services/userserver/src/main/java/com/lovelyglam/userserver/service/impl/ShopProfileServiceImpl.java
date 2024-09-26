package com.lovelyglam.userserver.service.impl;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;
import com.lovelyglam.database.model.entity.ShopProfile;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.userserver.service.ShopProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ShopProfileServiceImpl implements ShopProfileService {
    private final ShopRepository shopRepository;
    @Override
    public PaginationResponse<ShopProfileResponse> getShops(SearchRequestParamsDto request) {
        try {
            Page<ShopProfileResponse> orderPage = shopRepository.searchAnyByParameter(request.search(), request.pagination())
                    .map(item -> ShopProfileResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .phone(item.getPhone())
                            .avatarUrl(item.getAvatarUrl())
                            .thumbnails(item.getThumbnails())
                            .address(item.getAddress())
                            .build());
            return convert(orderPage);
        } catch (Exception  ex) {
            throw new ActionFailedException(
                    String.format("Get ShopProfiles failed with with reason: %s", ex.getMessage()));
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
        try{
            var item = shopRepository.save(shopProfile);
            return ShopProfileResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .phone(item.getPhone())
                    .avatarUrl(item.getAvatarUrl())
                    .thumbnails(item.getThumbnails())
                    .address(item.getAddress())
                    .build();
        } catch (Exception  ex) {
            throw new ActionFailedException(
                    String.format("Get ShopProfile failed with with reason: %s", ex.getMessage()));
        }
    }



}