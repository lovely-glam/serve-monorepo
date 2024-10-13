package com.lovelyglam.chatsocketserver.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found username"));
        return new User(userAccount.getUsername(),
                userAccount.getHashPassword(),
                userAccount.isActive(),
                true,
                true,
                true,
                rolesToAuthority(userAccount));
    }

    private Collection<GrantedAuthority> rolesToAuthority(UserAccount user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roleList;
    }
    
}
