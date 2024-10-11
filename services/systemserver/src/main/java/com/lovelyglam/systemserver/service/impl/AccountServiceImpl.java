package com.lovelyglam.systemserver.service.impl;

import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.database.repository.SystemAccountRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.email.service.MailSenderService;
import com.lovelyglam.systemserver.service.AccountService;

import java.math.BigDecimal;

import com.lovelyglam.systemserver.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserAccountRepository userAccountRepository;
    private final SystemAccountRepository systemAccountRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final AuthUtils authUtils;
    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;

    @Override
    public ProfileResponse disableUserAccount(BigDecimal id) {
        var systemAccount = authUtils.getUserAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var userAccountDb = userAccountRepository.findById(id);
        if (userAccountDb.isEmpty()) {
            throw new NotFoundException("User account not found");
        }
        userAccountDb.get().setActive(false);

        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("name", userAccountDb.get().getFullname());
                context.setVariable("message", "Your account has been disabled. If this was a mistake, please contact support.");
                String content = templateEngine.process("account-disabled-template", context); // Ensure you have this template
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(userAccountDb.get().getEmail());
                helper.setSubject("[Account Disabled - LOVELY GLAM]");
                helper.setText(content, true); // Set as HTML
            } catch (Exception ex) {
                throw new ActionFailedException("Failed to send account disabled email: " + ex.getMessage());
            }
        });
        try {
            var item = userAccountRepository.save(userAccountDb.get());

            return ProfileResponse.builder()
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed disable with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public ProfileResponse activeUserAccount(BigDecimal id) {
        var systemAccount = authUtils.getUserAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var userAccountDb = userAccountRepository.findById(id);
        if (userAccountDb.isEmpty()) {
            throw new NotFoundException("User account not found");
        }
        userAccountDb.get().setActive(true);
        try {
            var item = userAccountRepository.save(userAccountDb.get());

            return ProfileResponse.builder()
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed disable with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public ShopAccountResponse disableShopAccount(BigDecimal id) {
        var systemAccount = authUtils.getUserAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var shopAccountDb = shopAccountRepository.findById(id);
        if (shopAccountDb.isEmpty()) {
            throw new NotFoundException("Shop account not found");
        }
        shopAccountDb.get().setActive(false);
        return getShopAccountResponse(shopAccountDb.get());
    }

    @Override
    public ShopAccountResponse activeShopAccount(BigDecimal id) {
        var systemAccount = authUtils.getUserAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var shopAccountDb = shopAccountRepository.findById(id);
        if (shopAccountDb.isEmpty()) {
            throw new NotFoundException("Shop account not found");
        }
        shopAccountDb.get().setActive(true);
        return getShopAccountResponse(shopAccountDb.get());
    }

    private ShopAccountResponse getShopAccountResponse(ShopAccount shopAccountDb) {
        try{
            var item = shopAccountRepository.save(shopAccountDb);

            return ShopAccountResponse.builder()
                    .username(item.getUsername())
                    .id(item.getId())
                    .email(item.getEmail())
                    .isActive(item.isActive())
                    .isVerified(item.isVerified())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed with reason: %s", ex.getMessage()));
        }
    }


}
