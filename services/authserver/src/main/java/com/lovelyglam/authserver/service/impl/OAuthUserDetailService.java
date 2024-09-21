package com.lovelyglam.authserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.LoginMethodType;
import com.lovelyglam.database.model.entity.LoginMethod;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.repository.LoginMethodRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.utils.general.EnumUtils;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OAuthUserDetailService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
    private final LoginMethodRepository loginMethodRepository;
    private final UserAccountRepository userAccountRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String oauth2Id = oAuth2User.getName();
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        var loginType = EnumUtils.convertStringToEnum(LoginMethodType.class, oauth2Provider);
        LoginMethod user = loginMethodRepository.findLoginMethodByExternalIdAndLoginMethodType(
            oauth2Id, loginType
        ).orElseGet(() -> {
            var maxSize = userAccountRepository.count();
            var userAccountNew = UserAccount
            .builder()
            .hashPassword("")
            .build();
            var userLoginMethod = LoginMethod.builder()
            .loginType(loginType)
            .externalId(oauth2Id)
            .user(userAccountNew)
            .build();
            if ("google".equals(oauth2Provider)) {
                userAccountNew.setAvatarUrl(oAuth2User.getAttribute("picture"));
                userLoginMethod.setExternalEmail(oauth2Id);
                userAccountNew.setUsername(String.format("user-google@%s",maxSize));
                userAccountNew.setFullname(String.format("user-google@%s",maxSize));
            } else {
                userAccountNew.setAvatarUrl("https://graph.facebook.com/" + oAuth2User.getAttribute("id") + "/picture?type=large");
                userLoginMethod.setExternalEmail("");
                userAccountNew.setUsername(String.format("user-facebook@%s",maxSize));
                userAccountNew.setFullname(String.format("user-facebook@%s",maxSize));
            }
            userAccountNew.setLoginMethod(List.of(userLoginMethod));
            userAccountRepository.save(userAccountNew);
            return userLoginMethod;
        });
        var originAttribute = new HashMap<>(oAuth2User.getAttributes());
        originAttribute.put("userId", user.getUser().getId());
        originAttribute.put("method", oauth2Provider);
        return new DefaultOAuth2User(
                rolesToAuthority(user),
                originAttribute,
                "sub");
    }

    private Collection<GrantedAuthority> rolesToAuthority(LoginMethod user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roleList;
    }
    
}
