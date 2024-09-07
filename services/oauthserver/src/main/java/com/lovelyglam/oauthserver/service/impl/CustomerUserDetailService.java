package com.lovelyglam.oauthserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.entity.LoginMethod;
import com.lovelyglam.oauthserver.repository.LoginMethodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {
    private final LoginMethodRepository loginMethodRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = loginMethodRepository.findLoginMethodByExternalId(username).orElseThrow();
        return convertUserToUserDetailed(user);
    }

    private User convertUserToUserDetailed(LoginMethod loginMethod){
        return new User(loginMethod.getUsername(), loginMethod.getHashPassword(), rolesToAuthority(loginMethod) );
    }

    private Collection<GrantedAuthority> rolesToAuthority(LoginMethod user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.getUser().getShopProfile() != null) {
            roleList.add(new SimpleGrantedAuthority("ROLE_SHOP"));
        }
        return roleList;
    }
}
