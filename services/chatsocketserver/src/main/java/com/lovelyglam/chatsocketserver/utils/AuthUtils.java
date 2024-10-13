package com.lovelyglam.chatsocketserver.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lovelyglam.chatsocketserver.model.dto.ChatUser;
import com.lovelyglam.database.model.exception.AuthFailedException;
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
            auth.getAuthorities().stream().forEach(role -> {
                if (role.getAuthority().equals("ROLE_USER")) {
                    chatUser.setRole("ROLE_USER");
                    var user = userAccountRepository.findUserAccountByUsername(username).orElseThrow();
                    chatUser.setId(user.getId());
                    return;
                }
                if (role.getAuthority().equals("ROLE_SHOP")) {
                    chatUser.setRole("ROLE_SHOP");
                    var user = shopAccountRepository.findShopAccountByBusinessEmail(username).orElseThrow();
                    chatUser.setId(user.getId());
                    return;
                }
            });
            return chatUser;
        } catch (Exception ex) {
            throw new AuthFailedException("This user is't authentication, please login again");
        }
    }
}