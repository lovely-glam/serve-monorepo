package com.lovelyglam.userserver.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final UserAccountRepository userAccountRepository;
    public UserAccount getUserAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This user is't authentication, please login again");
            String username = auth.getName();
            return userAccountRepository.findUserAccountByUsername(username).orElseThrow();
        } catch (Exception ex) {
            throw new AuthFailedException("This user is't authentication, please login again");
        }
    }
}
