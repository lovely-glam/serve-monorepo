package com.lovelyglam.nailserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.response.ShopAccountResponse;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.nailserver.service.NailAccountService;
import com.lovelyglam.nailserver.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailAccountServiceImpl implements NailAccountService  {
    private final AuthUtils authUtils;
    private final ShopAccountRepository shopRepository;
    public ShopAccountResponse disableAccount () {
        var shopAccount = authUtils.getUserAccountFromAuthentication();
        shopAccount.setActive(false);
        shopRepository.save(shopAccount);
        return ShopAccountResponse.builder()
        
        .build();
    }
}
