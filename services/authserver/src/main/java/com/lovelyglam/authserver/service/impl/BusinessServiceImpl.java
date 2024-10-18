package com.lovelyglam.authserver.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.BusinessService;
import com.lovelyglam.database.model.dto.request.BusinessRegisterRequest;
import com.lovelyglam.database.model.dto.response.BusinessRegisterResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.entity.ShopProfile;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.utils.general.UrlUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    private final ShopAccountRepository shopAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BusinessRegisterResponse registerBusinessAccount(BusinessRegisterRequest request) {
        if (shopAccountRepository.findShopAccountByUsername(request.getUsername()).isPresent())
            throw new ValidationFailedException("Username is already in use");
        if (request.getPassword().equals(request.getRePassword())) {
            var user = ShopAccount.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .hashPassword(passwordEncoder.encode(request.getPassword()))
            .isActive(true)
            .isVerified(false)
            .build();
            var shopProfile = ShopProfile.builder()
            .vote(0.0)
            .name(request.getName())
            .address(request.getAddress())
            .avatarUrl(request.getAvatarUrl())
            .description(request.getDescription())
            .ownerFirstName(request.getOwnerFirstName())
            .ownerLastName(request.getOwnerLastName())
            .ownerPersonalIdentity(request.getOwnerPersonalIdentity())
            .businessCode(request.getBusinessCode())
            .thumbnails(UrlUtils.convertUrlListToString(request.getThumpNails(), ","))
            .account(user)
            .build();
            user.setShopProfile(shopProfile);
            try {
                var queryResult = shopAccountRepository.save(user);
                var shopProfileSave = queryResult.getShopProfile();
                return BusinessRegisterResponse.builder()
                    .username(queryResult.getUsername())
                    .name(shopProfileSave.getName())
                    .avatarUrl(shopProfileSave.getAvatarUrl())
                    .thumpNails(UrlUtils.convertUrlStringToList(shopProfile.getThumbnails(), ",").stream().toList())
                    .description(shopProfileSave.getDescription())
                    .email(queryResult.getEmail())
                    .address(shopProfile.getAddress())
                    .createdDate(queryResult.getCreatedDate())
                .build();
            } catch (Exception ex) {
                throw new ActionFailedException(String.format("Failed to adding register business account with reason %s", ex.getMessage()));
            }
        } else {
            throw new ValidationFailedException("Password And RePassword Is Not Match, Please Check Again");
        }
    }

    public ShopAccountResponse verifyBusinessAccount(String businessEmail) {
        var queryResult = shopAccountRepository.findShopAccountByBusinessEmail(businessEmail).orElseThrow(
            () -> new ActionFailedException("Failed to verify this account with reason")
        );
        queryResult.setVerified(true);
        try {
            shopAccountRepository.save(queryResult);
            return ShopAccountResponse.builder()
            .username(queryResult.getUsername())
            .id(queryResult.getId())
            .email(queryResult.getEmail())
            .isActive(queryResult.isActive())
            .isVerified(true)
            .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Failed to active account");
        }
    }

    @Override
    public ShopAccountResponse isIdentityExisted(String identity) {
        var queryResult = shopAccountRepository.findShopAccountByBusinessEmail(identity).orElseThrow(() -> new NotFoundException("Not found account"));
        return ShopAccountResponse.builder()
            .username(queryResult.getUsername())
            .id(queryResult.getId())
            .email(queryResult.getEmail())
            .isActive(queryResult.isActive())
            .isVerified(true)
            .build();
    }
}
