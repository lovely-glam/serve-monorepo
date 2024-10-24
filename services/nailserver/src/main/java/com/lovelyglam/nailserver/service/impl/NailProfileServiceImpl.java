package com.lovelyglam.nailserver.service.impl;

import java.util.Collections;
import java.util.List;

import com.lovelyglam.utils.general.TextUtils;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.ShopDetailRequest;
import com.lovelyglam.database.model.dto.response.NailProfileDetailResponse;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;
import com.lovelyglam.database.model.entity.ShopService;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.nailserver.service.NailProfileService;
import com.lovelyglam.nailserver.utils.AuthUtils;
import com.lovelyglam.utils.general.UrlUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailProfileServiceImpl implements NailProfileService {
    private final ShopRepository shopRepository;
    private final AuthUtils authUtils;

    public NailProfileDetailResponse getMe () {
        var shopAccount = authUtils.getUserAccountFromAuthentication();
        var shopProfile = shopAccount.getShopProfile();
        return NailProfileDetailResponse.builder()
        .id(shopProfile.getId())
        .name(shopProfile.getName())
        .avatarUrl(shopProfile.getAvatarUrl())
        .address(shopProfile.getAddress())
        .thumbnails(TextUtils.extractValidUrls(shopProfile.getThumbnails()))
        .phone(shopProfile.getPhone())
        .nailServices(convertShopServiceToNailServiceResponse(shopProfile.getShopServices()))
        .build();
    }

    public ShopProfileResponse updateProfile (ShopDetailRequest shopUpdateRequest) {
        if (shopUpdateRequest == null) throw new ValidationFailedException("The request is not valid");
        var thumbnails = UrlUtils.convertUrlListToString(shopUpdateRequest.getThumbnails(), ",");
        var shopEntity = authUtils.getUserAccountFromAuthentication().getShopProfile();
        shopEntity.setAddress(shopUpdateRequest.getAddress());
        shopEntity.setName(shopUpdateRequest.getName());
        shopEntity.setAvatarUrl(shopUpdateRequest.getAvatarUrl());
        shopEntity.setThumbnails(thumbnails);
        try {
            shopRepository.save(shopEntity);
            return ShopProfileResponse.builder()
            .id(shopEntity.getId())
            .name(shopEntity.getName())
            .avatarUrl(shopEntity.getAvatarUrl())
            .address(shopEntity.getAddress())
            .phone(shopEntity.getPhone())
            .build();
        }catch(Exception ex) {
            throw new ActionFailedException(String.format("Failed to update shop profile with reason %s", ex.getMessage()),ex);
        }
        
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
