package com.lovelyglam.chatsocketserver.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lovelyglam.chatsocketserver.model.dto.ChatUser;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.database.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final ShopAccountRepository shopAccountRepository;
    private final UserAccountRepository userAccountRepository;

    public ChatUser getUserAccountFromAuthentication() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null) throw new AuthFailedException("This user is't authentication, please login again");
            String username = auth.getName();
            var chatUser = ChatUser.builder()
            .username(username)
            .build();
            auth.getAuthorities().stream()
            .map(role -> role.getAuthority())
            .filter(authority -> authority.equals("ROLE_USER") || authority.equals("ROLE_SHOP"))
            .findFirst()
            .ifPresentOrElse(authority -> {
                if (authority.equals("ROLE_USER")) {
                    chatUser.setRole("ROLE_USER");
                    var user = userAccountRepository.findUserAccountByUsername(username)
                            .orElseThrow(() -> new AuthFailedException("User account not found"));
                    chatUser.setId(user.getId());
                } else if (authority.equals("ROLE_SHOP")) {
                    chatUser.setRole("ROLE_SHOP");
                    var user = shopAccountRepository.findShopAccountByUsername(username)
                            .orElseThrow(() -> new AuthFailedException("Shop account not found"));
                    chatUser.setId(user.getId());
                }
            }, () -> {
                throw new AuthFailedException("User has no valid roles assigned");
            });

            return chatUser;
        } catch (Exception ex) {
            throw new AuthFailedException("This user is't authentication, please login again");
        }
    }

    public UserAccount getUserAccountById (ChatUser input) {
        if ("ROLE_USER".equals(input.getRole())) {
            return userAccountRepository.findById(input.getId()).orElseThrow(() -> new NotFoundException("Not found user with this id"));
        } 
        else throw new ValidationFailedException("");
    }

    public ShopAccount getBusinessAccountById (ChatUser input) {
        if ("ROLE_SHOP".equals(input.getRole())) {
            return shopAccountRepository.findById(input.getId()).orElseThrow(() -> new NotFoundException("Not found user with this id"));
        } 
        else throw new ValidationFailedException("");
    }
}
