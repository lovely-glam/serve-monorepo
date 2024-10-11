package com.lovelyglam.systemserver.util;

import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.database.repository.SystemAccountRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final UserAccountRepository userAccountRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final SystemAccountRepository systemAccountRepository;
    public UserAccount getUserAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This user is't authentication, please login again");
            String username = auth.getName();
            return userAccountRepository.findUserAccountByUsername(username).orElseThrow();
        } catch (Exception ex) {
            throw new AuthFailedException("This user isn't authentication, please login again");
        }
    }
    public ShopAccount getShopAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This account is't authentication, please login again");
            String username = auth.getName();
            return shopAccountRepository.findShopAccountByUsername(username).orElseThrow();
        } catch (Exception ex) {
            throw new AuthFailedException("This account is't authentication, please login again");
        }
    }
    public SystemAccount getSystemAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This account isn't authentication, please login again");
            String username = auth.getName();
            return systemAccountRepository.findSystemAccountByUsername(username).orElseThrow();
        } catch (Exception ex) {
            throw new AuthFailedException("This account isn't authentication, please login again");
        }
    }
}
