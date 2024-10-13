package com.lovelyglam.chatsocketserver.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.repository.ShopAccountRepository;

import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService{
    private final ShopAccountRepository shopAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ShopAccount userAccount = shopAccountRepository.findShopAccountByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found username"));
        return new User(userAccount.getUsername(),userAccount.getHashPassword(),rolesToAuthority(userAccount));
    }

    private Collection<GrantedAuthority> rolesToAuthority(ShopAccount user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_NAILER"));
        return roleList;
    }
    
}
