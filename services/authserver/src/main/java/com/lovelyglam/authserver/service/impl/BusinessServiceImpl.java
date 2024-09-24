package com.lovelyglam.authserver.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.BusinessService;
import com.lovelyglam.database.model.dto.request.BusinessRegisterRequest;
import com.lovelyglam.database.model.dto.response.BusinessRegisterResponse;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.entity.ShopProfile;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    private final ShopAccountRepository shopAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BusinessRegisterResponse registerCustomerAccount(BusinessRegisterRequest request) {
        if (request.getPassword().equals(request.getRePassword())) {
            var user = ShopAccount.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .hashPassword(passwordEncoder.encode(request.getPassword()))
            .isActive(true)
            .isVerified(true)
            .build();
            var shopProfile = ShopProfile.builder()
            .address(request.getAddress())
            .avatarUrl(request.getAvatarUrl())
            .description(request.getDescription())
            .thumbnails(null)
            .account(user)
            .build();
            user.setShopProfile(shopProfile);
            try {
                var queryResult = shopAccountRepository.save(user);
                return BusinessRegisterResponse.builder()
                    .createdDate(queryResult.getCreatedDate())
                    .username(queryResult.getUsername())
                    .address(shopProfile.getAddress())
                    .avatarUrl(shopProfile.getAvatarUrl())
                    .description(shopProfile.getDescription())
                .build();
            } catch (Exception ex) {
                throw new ActionFailedException(String.format("Failed to adding register business account with reason %s", ex.getMessage()));
            }
        } else {
            throw new ValidationFailedException("Password And Repassword Is Not Match, Please Check Again");
        }
    }
}
