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
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.email.service.MailSenderService;
import com.lovelyglam.systemserver.service.NailAccountService;
import com.lovelyglam.systemserver.util.AuthUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class NailAccountServiceImpl implements NailAccountService {
    private final ShopAccountRepository shopAccountRepository;
    private final AuthUtils authUtils;
    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;
    @Override
    public ShopAccountResponse disableShopAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var shopAccountDb = shopAccountRepository.findById(id);
        if (shopAccountDb.isEmpty()) {
            throw new NotFoundException("Shop account not found");
        }
        if (!shopAccountDb.get().isActive())
            throw new ValidationFailedException("Shop account is not active");
        shopAccountDb.get().setActive(false);
        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("name", shopAccountDb.get().getShopProfile().getName());
                context.setVariable("message", "Your account has been disabled. If this was a mistake, please contact support.");
                context.setVariable("Support", "https://lovelyglam.life/contact");
                String content = templateEngine.process("account-disabled-email", context); // Ensure you have this template
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(shopAccountDb.get().getEmail());
                helper.setSubject("[Account Disabled - LOVELY GLAM]");
                helper.setText(content, true); // Set as HTML
            } catch (Exception ex) {
                throw new ActionFailedException("Failed to send account disabled email: " + ex.getMessage(), ex);
            }
        });
        return getShopAccountResponse(shopAccountDb.get());
    }

    @Override
    public ShopAccountResponse activeShopAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        var shopAccountDb = shopAccountRepository.findById(id);
        if (shopAccountDb.isEmpty()) {
            throw new NotFoundException("Shop account not found");
        }
        if (shopAccountDb.get().isActive())
            throw new ValidationFailedException("Shop account is active");
        shopAccountDb.get().setActive(true);

        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("name", shopAccountDb.get().getShopProfile().getName());
                context.setVariable("message", "Your account has been disabled. If this was a mistake, please contact support.");
                context.setVariable("login","https://lovelyglam.life/login");
                String content = templateEngine.process("account-reactivation-email", context); // Ensure you have this template
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(shopAccountDb.get().getEmail());
                helper.setSubject("[Account Disabled - LOVELY GLAM]");
                helper.setText(content, true); // Set as HTML
            } catch (Exception ex) {
                throw new ActionFailedException("Failed to send account disabled email: " + ex.getMessage());
            }
        });
        return getShopAccountResponse(shopAccountDb.get());
    }

    @Override
    public ShopAccountResponse getShopAccount(BigDecimal id) {
        var systemAccount = authUtils.getSystemAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this function");
        }
        ShopAccount shopAccountDb = shopAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found Shop Account"));
        try {
            var item = shopAccountRepository.save(shopAccountDb);
            return ShopAccountResponse.builder()
                    .id(item.getId())
                    .username(item.getUsername())
                    .email(item.getEmail())
                    .isActive(item.isActive())
                    .isVerified(item.isVerified())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed to get shop account with reason: %s", ex.getMessage()), ex);
        }
    }

    @Override
    public PaginationResponse<ShopAccountResponse> getShopAccounts(SearchRequestParamsDto request) {
        return shopAccountRepository.searchByParameter(request.search(), request.pagination(),(entity) -> {
            return ShopAccountResponse.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .email(entity.getEmail())
                    .isActive(entity.isActive())
                    .isVerified(entity.isVerified())
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
            throw new ActionFailedException(String.format("Failed with reason: %s", ex.getMessage()), ex);
        }

    }
}
