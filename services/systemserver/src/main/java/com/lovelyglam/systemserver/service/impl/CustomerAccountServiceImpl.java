package com.lovelyglam.systemserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.CustomerAccountManagementResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.email.service.MailSenderService;
import com.lovelyglam.systemserver.service.CustomerAccountService;
import com.lovelyglam.systemserver.util.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private final UserAccountRepository userAccountRepository;
    private final AuthUtils authUtils;
    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;

    @Override
    public ProfileResponse disableUserAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var userAccountDb = userAccountRepository.findById(id);
        if (userAccountDb.isEmpty()) {
            throw new NotFoundException("User account not found");
        }
        if (!userAccountDb.get().isActive())
            throw new ValidationFailedException("User account is not active");
        userAccountDb.get().setActive(false);

        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("name", userAccountDb.get().getFullname());
                context.setVariable("message", "Your account has been disabled. If this was a mistake, please contact support.");
                context.setVariable("Support", "https://lovelyglam.life/contact");
                String content = templateEngine.process("account-disabled-template", context);
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(userAccountDb.get().getEmail());
                helper.setSubject("[Account Disabled - LOVELY GLAM]");
                helper.setText(content, true);
            } catch (Exception ex) {
                throw new ActionFailedException("Failed to send account disabled email: " + ex.getMessage(), ex);
            }
        });

        try {
            var item = userAccountRepository.save(userAccountDb.get());

            return ProfileResponse.builder()
                    .id(item.getId())
                    .fullName(item.getFullname())
                    .username(item.getUsername())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed disable with reason: %s", ex.getMessage()),ex);
        }
    }

    @Override
    public ProfileResponse activeUserAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var userAccountDb = userAccountRepository.findById(id);
        if (userAccountDb.isEmpty()) {
            throw new NotFoundException("User account not found");
        }
        if (userAccountDb.get().isActive())
            throw new ValidationFailedException("User account is active");
        userAccountDb.get().setActive(true);

        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("name", userAccountDb.get().getFullname());
                context.setVariable("message", "Your account has been reactivated. If this was a mistake, please contact support.");
                context.setVariable("login","https://lovelyglam.life/login");
                String content = templateEngine.process("account-reactivation-email", context); // Ensure you have this template
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(userAccountDb.get().getEmail());
                helper.setSubject("[Account ReActive - LOVELY GLAM]");
                helper.setText(content, true);
            } catch (Exception ex) {
                throw new ActionFailedException("Failed to send account disabled email: " + ex.getMessage(), ex);
            }
        });

        try {
            var item = userAccountRepository.save(userAccountDb.get());

            return ProfileResponse.builder()
                    .id(item.getId())
                    .username(item.getUsername())
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed disable with reason: %s", ex.getMessage()), ex);
        }
    }



    @Override
    public ProfileResponse getUserAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        UserAccount userAccountDb = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found User Account"));
        try {
            var item = userAccountRepository.save(userAccountDb);
            return ProfileResponse.builder()
                    .id(item.getId())
                    .username(item.getUsername())
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed to get user account with reason: %s", ex.getMessage()), ex);
        }
    }

    @Override
    public PaginationResponse<CustomerAccountManagementResponse> getCustomerAccounts(SearchRequestParamsDto request) {
        return userAccountRepository.searchByParameter(request.search(), request.pagination(),(entity) -> {
            return CustomerAccountManagementResponse.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .status(entity.isActive())
            .createdDate(entity.getCreatedDate())
            .fullName(entity.getFullname())
            .build();
        }, (param) -> {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                            "%" + item.getValue().toLowerCase() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        });
    }
}
