package com.lovelyglam.authserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.lovelyglam.database.repository.LoginMethodRepository;
import com.lovelyglam.utils.general.EnumUtils;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OAuthUserDetailService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
    private final LoginMethodRepository loginMethodRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String oauth2Id = oAuth2User.getName();
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        LoginMethod user = loginMethodRepository.findLoginMethodByExternalIdAndLoginMethodType(oauth2Id,EnumUtils.convertStringToEnum(LoginMethodType.class, oauth2Provider)).orElseThrow();
        return new DefaultOAuth2User(
                rolesToAuthority(user),
                oAuth2User.getAttributes(),
                "sub");
    }

    private Collection<GrantedAuthority> rolesToAuthority(LoginMethod user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roleList;
    }
    
}
