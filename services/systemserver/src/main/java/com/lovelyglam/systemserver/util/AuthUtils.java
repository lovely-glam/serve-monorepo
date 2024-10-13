package com.lovelyglam.systemserver.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.SystemAccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final SystemAccountRepository systemAccountRepository;
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
