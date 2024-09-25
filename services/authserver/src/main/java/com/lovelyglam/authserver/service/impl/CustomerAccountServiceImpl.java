package com.lovelyglam.authserver.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.CustomerAccountService;
import com.lovelyglam.database.model.dto.request.CustomerRegisterRequest;
import com.lovelyglam.database.model.dto.response.CustomerRegisterResponse;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
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
            .fullname(request.getFullName())
            .hashPassword(passwordEncoder.encode(request.getPassword()))
            .build();
            try {
                var queryResult = userAccountRepository.save(user);
                return CustomerRegisterResponse.builder()
                    .email(queryResult.getEmail())
                    .createdDate(queryResult.getCreatedDate())
                    .username(queryResult.getUsername())
                    .fullName(queryResult.getFullname())
                .build();
            } catch (Exception ex) {
                throw new ActionFailedException(String.format("Failed to adding register customer account with reason %s", ex.getMessage()));
            }
        } else {
            throw new ValidationFailedException("Password And Repassword Is Not Match, Please Check Again");
        }
    }
}