package com.lovelyglam.nailserver.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final ShopAccountRepository shopAccountRepository;
    public ShopAccount getUserAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This user is't authentication, please login again");
            String username = auth.getName();
            return shopAccountRepository.findShopAccountByUsername(username).orElseThrow();
        } catch (Exception ex) {
            throw new AuthFailedException("This user is't authentication, please login again");
        }
    }
}
