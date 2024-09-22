package com.lovelyglam.authserver.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.CustomerAccountService;
import com.lovelyglam.database.model.dto.request.CustomerRegisterRequest;
import com.lovelyglam.database.model.dto.response.CustomerRegisterResponse;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerRegisterResponse registerCustomerAccount(CustomerRegisterRequest request) {
        if (request.getPassword().equals(request.getRePassword())) {
            var user = UserAccount.builder()
            .avatarUrl("")
            .email(request.getEmail())
            .username(request.getUsername())
            .hashPassword(passwordEncoder.encode(request.getPassword()))
            .build();
            try {
                userAccountRepository.save(user);
            } catch (Exception ex) {
                
            }
            
        }
        return null;
    }
}
